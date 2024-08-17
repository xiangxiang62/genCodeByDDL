package main.java.com.xiangxiang.genCodeByDDL.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import main.java.com.xiangxiang.genCodeByDDL.builder.TableSchemaBuilder;
import main.java.com.xiangxiang.genCodeByDDL.model.GenerateBySQLVO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 插件主启动类，用于处理 SQL 文件并生成 Java 代码
 */
public class GenCodeByDDLAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        // 获取当前项目
        Project project = event.getProject();
        if (project == null) {
            Messages.showMessageDialog("找不到项目。", "错误", Messages.getErrorIcon());
            return;
        }

        // 获取当前选中的文件
        VirtualFile file = event.getDataContext().getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE);
        if (file != null && "sql".equals(file.getExtension())) {
            try {
                // 读取 SQL 文件内容
                String fileContent = new String(file.contentsToByteArray(), StandardCharsets.UTF_8);

                // 根据 SQL 文件内容生成 Java 代码
                GenerateBySQLVO generateBySQLVO = TableSchemaBuilder.buildFromDDL(fileContent);
                List<String> javaControllerCode = generateBySQLVO.getJavaControllerCode();
                List<String> javaAddEntityCode = generateBySQLVO.getJavaAddEntityCode();
                List<String> javaEditEntityCode = generateBySQLVO.getJavaEditEntityCode();
                List<String> javaEntityCodeList = generateBySQLVO.getJavaEntityCode();

                // 弹出对话框让用户选择生成的代码类型
                CodeGenerationDialog dialog = new CodeGenerationDialog(project);
                dialog.show();
                if (dialog.isOK()) {
                    // 获取用户选择的代码类型
                    Map<String, Boolean> selectedOptions = dialog.getSelectedOptions();

                    // 执行写操作的代码
                    WriteCommandAction.runWriteCommandAction(project, () -> {
                        try {
                            VirtualFile projectRoot = getProjectRoot(project);
                            if (projectRoot == null) return;

                            // 创建 `generator` 目录
                            VirtualFile generatorDir = getGeneratorDir(projectRoot, "generator");

                            // 根据用户选择生成相应的代码
                            if (selectedOptions.getOrDefault("controller", false)) {
                                createCodeFiles(javaControllerCode, generatorDir, "controller");
                            }
                            if (selectedOptions.getOrDefault("model", false)) {
                                createCodeFiles(javaEntityCodeList, generatorDir, "model");
                            }
                            if (selectedOptions.getOrDefault("dto", false)) {
                                createDTOFiles(javaAddEntityCode, generatorDir);
                            }
                            if (selectedOptions.getOrDefault("dto", false)) {
                                createDTOFiles(javaEditEntityCode, generatorDir);
                            }

                            // 在写操作完成后显示消息对话框
                            ApplicationManager.getApplication().invokeLater(() -> {
                                Messages.showMessageDialog("代码已生成并保存。", "成功", Messages.getInformationIcon());
                            });
                        } catch (IOException e) {
                            ApplicationManager.getApplication().invokeLater(() -> {
                                Messages.showMessageDialog("写入文件失败: " + e.getMessage(), "错误", Messages.getErrorIcon());
                            });
                        }
                    });
                }
            } catch (IOException e) {
                ApplicationManager.getApplication().invokeLater(() -> {
                    Messages.showMessageDialog("读取文件失败: " + e.getMessage(), "错误", Messages.getErrorIcon());
                });
            }
        } else {
            ApplicationManager.getApplication().invokeLater(() -> {
                Messages.showMessageDialog("此操作仅适用于 .sql 文件。", "错误", Messages.getErrorIcon());
            });
        }
    }

    /**
     * 获取项目的根目录
     *
     * @param project 当前项目
     * @return 项目的根目录，如果找不到则返回 null
     */
    @Nullable
    private VirtualFile getProjectRoot(Project project) {
        VirtualFile projectRoot = project.getBaseDir();
        if (projectRoot == null) {
            Messages.showMessageDialog("找不到项目根目录。", "错误", Messages.getErrorIcon());
            return null;
        }
        return projectRoot;
    }

    /**
     * 获取或创建 `generator` 目录
     *
     * @param projectRoot 项目的根目录
     * @param generator   目录名
     * @return `generator` 目录
     * @throws IOException 如果创建目录失败
     */
    @NotNull
    private VirtualFile getGeneratorDir(VirtualFile projectRoot, String generator) throws IOException {
        VirtualFile generatorDir = projectRoot.findChild(generator);
        if (generatorDir == null) {
            generatorDir = projectRoot.createChildDirectory(this, generator);
        }
        return generatorDir;
    }

    /**
     * 创建 Controller、Model 或 DTO 代码文件并写入到对应的目录
     *
     * @param javaCodeList 需要创建的 Java 代码列表
     * @param generatorDir 目标目录
     * @param subPackage   子包名（例如 "controller"、"model"）
     * @throws IOException 如果写入文件失败
     */
    private void createCodeFiles(List<String> javaCodeList, VirtualFile generatorDir, String subPackage) throws IOException {
        // 获取或创建子包目录
        VirtualFile subPackageDir = getGeneratorDir(generatorDir, subPackage);

        VirtualFile entityDir = getGeneratorDir(subPackageDir, "entity");

        // 将每个 Java 代码写入对应的文件
        for (String javaCode : javaCodeList) {
            String className = extractClassName(javaCode);
            if (className != null) {
                VirtualFile newFile = entityDir.createChildData(this, className + ".java");
                newFile.setBinaryContent(javaCode.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    /**
     * 创建 DTO 代码文件，并将其放置在 model.dto 子包中，按表名分类
     *
     * @param javaCodeList 需要创建的 DTO 代码列表
     * @param generatorDir 目标目录
     * @throws IOException 如果写入文件失败
     */
    private void createDTOFiles(List<String> javaCodeList, VirtualFile generatorDir) throws IOException {
        // 获取或创建 model 目录
        VirtualFile modelDir = getGeneratorDir(generatorDir, "model");
        // 获取或创建 dto 目录
        VirtualFile dtoDir = getGeneratorDir(modelDir, "dto");

        // 将每个 DTO 代码写入对应的文件夹
        for (String javaCode : javaCodeList) {
            String className = extractClassName(javaCode);
            if (className != null) {
                // 假设类名格式为 tableNameAddRequest，提取表名部分
                String tableName = className.replaceAll("(AddRequest|EditRequest)$", "").toLowerCase();// 提取表名并转换为小写
                // 创建表名子目录
                VirtualFile tableDir = getGeneratorDir(dtoDir, tableName);
                // 创建 DTO 文件
                VirtualFile newFile = tableDir.createChildData(this, className + ".java");
                newFile.setBinaryContent(javaCode.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    /**
     * 从 Java 代码中提取类名
     *
     * @param javaCode Java 代码
     * @return 提取的类名，如果无法提取则返回 null
     */
    private String extractClassName(String javaCode) {
        String[] lines = javaCode.split("\\n");
        for (String line : lines) {
            if (line.startsWith("public class")) {
                String[] parts = line.split("\\s+");
                if (parts.length > 2) {
                    return parts[2];
                }
            }
        }
        return null;
    }

    /**
     * 自定义对话框类，用于选择生成的代码类型
     */
    private static class CodeGenerationDialog extends DialogWrapper {

        private JCheckBox controllerCheckBox;
        private JCheckBox modelCheckBox;
        private JCheckBox dtoCheckBox;
        private final Map<String, Boolean> selectedOptions = new HashMap<>();

        /**
         * 构造函数，初始化对话框
         *
         * @param project 当前项目
         */
        protected CodeGenerationDialog(@Nullable Project project) {
            super(project);
            setTitle("选择要生成的代码类型"); // 设置对话框标题
            init(); // 初始化对话框
        }

        /**
         * 创建对话框的中心面板，该面板包含选项卡供用户选择要生成的代码类型。
         * <p>
         * 该方法创建了一个包含两个选项卡的面板：
         * <ul>
         *     <li>逻辑相关：包含一个复选框，用于选择是否生成 Controller 代码</li>
         *     <li>实体类相关：包含两个复选框，分别用于选择是否生成 Model 代码和 DTO 代码</li>
         * </ul>
         * </p>
         *
         * @return 创建的中心面板，包含选项卡和相关控件
         */
        @Override
        protected JComponent createCenterPanel() {
            // 创建选项卡面板，用于展示多个选项卡
            JTabbedPane tabbedPane = new JTabbedPane();

            // 创建 Controller 选项卡面板
            JPanel controllerPanel = new JPanel();
            controllerPanel.setLayout(new BoxLayout(controllerPanel, BoxLayout.Y_AXIS)); // 设置布局管理器为垂直排列
            controllerPanel.setPreferredSize(new Dimension(600, 450)); // 设置选项卡的首选大小
            controllerCheckBox = new JCheckBox("Controller"); // 创建一个复选框，用于选择是否生成 Controller 代码
            controllerPanel.add(controllerCheckBox); // 将复选框添加到 Controller 选项卡面板中

            // 创建 Model 选项卡面板，包括 DTO 选项
            JPanel modelPanel = new JPanel();
            modelPanel.setLayout(new BoxLayout(modelPanel, BoxLayout.Y_AXIS)); // 设置布局管理器为垂直排列
            modelPanel.setPreferredSize(new Dimension(600, 450)); // 设置选项卡的首选大小
            modelCheckBox = new JCheckBox("Model"); // 创建一个复选框，用于选择是否生成 Model 代码
            dtoCheckBox = new JCheckBox("DTO"); // 创建一个复选框，用于选择是否生成 DTO 代码
            modelPanel.add(modelCheckBox); // 将 Model 复选框添加到 Model 选项卡面板中
            modelPanel.add(dtoCheckBox); // 将 DTO 复选框添加到 Model 选项卡面板中

            // 将面板添加到选项卡中
            tabbedPane.addTab("逻辑相关", controllerPanel);
            tabbedPane.addTab("实体类相关", modelPanel);

            return tabbedPane;
        }

        /**
         * 确认对话框操作时调用的方法。此方法将获取用户在对话框中选择的代码生成选项，
         * 并将选项存储到 `selectedOptions` 映射中，以便后续使用。
         * <p>
         * 调用此方法时会记录用户的选择状态，选择的选项包括：
         * <ul>
         *     <li>"controller"：是否选择生成 Controller 代码</li>
         *     <li>"model"：是否选择生成 Model 代码</li>
         *     <li>"dto"：是否选择生成 DTO 代码</li>
         * </ul>
         * </p>
         */
        @Override
        protected void doOKAction() {
            // 获取用户选择的选项，并存储到 `selectedOptions` 中
            selectedOptions.put("controller", controllerCheckBox.isSelected());
            selectedOptions.put("model", modelCheckBox.isSelected());
            selectedOptions.put("dto", dtoCheckBox.isSelected());
            super.doOKAction(); // 调用父类方法确认对话框操作
        }

        /**
         * 获取用户选择的选项
         *
         * @return 用户选择的选项及其状态
         */
        public Map<String, Boolean> getSelectedOptions() {
            return selectedOptions;
        }
    }
}
