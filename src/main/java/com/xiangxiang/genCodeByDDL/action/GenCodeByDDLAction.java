package main.java.com.xiangxiang.genCodeByDDL.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import main.java.com.xiangxiang.genCodeByDDL.builder.TableSchemaBuilder;
import main.java.com.xiangxiang.genCodeByDDL.model.GenerateBySQLVO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
                // 刷新文件内容，确保读取的是最新内容
                file.refresh(false, false);

                // 读取 SQL 文件内容
                String fileContent = new String(file.contentsToByteArray(), StandardCharsets.UTF_8);

                // 根据 SQL 文件内容生成 Java 代码
                GenerateBySQLVO generateBySQLVO = TableSchemaBuilder.buildFromDDL(fileContent);
                // 获取控制层代码
                List<String> javaControllerCode = generateBySQLVO.getJavaControllerCode();
                // 获取 Service 层代码
                List<String> javaServiceCode = generateBySQLVO.getJavaServiceCode();
                // 获取新增 DTO 代码
                List<String> javaAddEntityCode = generateBySQLVO.getJavaAddEntityCode();
                // 获取编辑 DTO 代码
                List<String> javaEditEntityCode = generateBySQLVO.getJavaEditEntityCode();
                // 获取查询 DTO 代码
                List<String> javaQueryEntityCode = generateBySQLVO.getJavaQueryEntityCode();
                // 获取更新 DTO 代码
                List<String> javaUpdateEntityCode = generateBySQLVO.getJavaUpdateEntityCode();
                // 获取实体类代码
                List<String> javaEntityCodeList = generateBySQLVO.getJavaEntityCode();
                // 获取视图类代码
                List<String> javaEntityVOCodeList = generateBySQLVO.getJavaEntityVOCode();
                // 获取持久层代码
                List<String> javaMapperCode = generateBySQLVO.getJavaMapperCode();
                // 获取 mapperXml 代码
                List<String> mapperXmlCode = generateBySQLVO.getMapperXmlCode();

                // 获取 README.md 文件
                String README = generateBySQLVO.getREADME();

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

                            // 创建 generator 目录
                            VirtualFile generatorDir = getGeneratorDir(projectRoot, "generator");

                            // 根据用户选择生成相应的代码
                            // 生成 controller
                            if (selectedOptions.getOrDefault("controller", false)) {
                                createCodeFiles(javaControllerCode, generatorDir, "controller", null);
                            }
                            // 生成 service
                            if (selectedOptions.getOrDefault("service", false)) {
                                createCodeFiles(javaServiceCode, generatorDir, "service", null);
                            }
                            // 生成 entity 实体类
                            if (selectedOptions.getOrDefault("model", false)) {
                                createCodeFiles(javaEntityCodeList, generatorDir, "model", "entity");
                            }
                            // 生成 addRequest
                            if (selectedOptions.getOrDefault("dto", false)) {
                                createDTOFiles(javaAddEntityCode, generatorDir);
                            }
                            // 生成 editRequest
                            if (selectedOptions.getOrDefault("dto", false)) {
                                createDTOFiles(javaEditEntityCode, generatorDir);
                            }
                            // 生成 queryRequest
                            if (selectedOptions.getOrDefault("dto", false)) {
                                createDTOFiles(javaQueryEntityCode, generatorDir);
                            }
                            // 生成 updateRequest
                            if (selectedOptions.getOrDefault("dto", false)) {
                                createDTOFiles(javaUpdateEntityCode, generatorDir);
                            }
                            // 生成插件自述 readme.md 文件
                            if (selectedOptions.getOrDefault("readme", false)) {
                                createREADMEFile(Collections.singletonList(README), projectRoot);
                            }
                            // 生成 vo 视图
                            if (selectedOptions.getOrDefault("vo", false)) {
                                createCodeFiles(javaEntityVOCodeList, generatorDir, "model", "vo");
                            }
                            // 生成持久层 mapper
                            if (selectedOptions.getOrDefault("mapper", false)) {
                                createCodeFiles(javaMapperCode, generatorDir, "Mapper", "");
                            }
                            // 生成 mapper.xml 配置
                            if (selectedOptions.getOrDefault("mapperXml", false)) {
                                createConfigCodeFiles(mapperXmlCode, generatorDir, "MapperXml");
                            }

                            // 在写操作完成后显示消息对话框
                            ApplicationManager.getApplication().invokeLater(() -> {
                                Messages.showMessageDialog("代码已生成并保存，生成位置：./generator。", "成功", Messages.getInformationIcon());
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
     * 获取或创建 generator 目录
     *
     * @param projectRoot 项目的根目录
     * @param generator   目录名
     * @return generator 目录
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
    private void createCodeFiles(List<String> javaCodeList, VirtualFile generatorDir, String subPackage, String generator) throws IOException {
        // 获取或创建子包目录
        VirtualFile subPackageDir;
        if (subPackage.equals("")) {
            subPackageDir = generatorDir;
        } else {
            subPackageDir = getGeneratorDir(generatorDir, subPackage);
        }

        VirtualFile entityDir = subPackageDir;

        if ("dto".equals(generator) || "vo".equals(generator) || "entity".equals(generator)) {
            entityDir = getGeneratorDir(subPackageDir, generator);
        }

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
     * 创建 Controller、Model 或 DTO 代码文件并写入到对应的目录
     *
     * @param javaCodeList 需要创建的 Java 代码列表
     * @param generatorDir 目标目录
     * @param subPackage   子包名（例如 "controller"、"model"）
     * @throws IOException 如果写入文件失败
     */
    private void createConfigCodeFiles(List<String> javaCodeList, VirtualFile generatorDir, String subPackage) throws IOException {
        // 获取或创建子包目录
        VirtualFile subPackageDir;
        subPackageDir = getGeneratorDir(generatorDir, subPackage);

        // 将每个代码写入对应的文件
        for (String javaCode : javaCodeList) {
            String className = extractClassNameFromXML(javaCode);
            if (className != null) {
                VirtualFile newFile = subPackageDir.createChildData(this, className + ".xml");
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
                String tableName = className.replaceAll("(AddRequest|EditRequest|QueryRequest|UpdateRequest)$", "").toLowerCase();// 提取表名并转换为小写
                // 创建表名子目录
                VirtualFile tableDir = getGeneratorDir(dtoDir, tableName);
                // 创建 DTO 文件
                VirtualFile newFile = tableDir.createChildData(this, className + ".java");
                newFile.setBinaryContent(javaCode.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    /**
     * 创建 readme 文件
     * @param javaCodeList 数据
     * @param generatorDir 生成目录
     * @throws IOException
     */
    private void createREADMEFile(List<String> javaCodeList, VirtualFile generatorDir) throws IOException {
        VirtualFile readmeDir = getGeneratorDir(generatorDir, "generator");

        // 将每个 DTO 代码写入对应的文件夹
        for (String javaCode : javaCodeList) {
            String className = "README";
            // 创建文件
            VirtualFile newFile = readmeDir.createChildData(this, className + ".md");
            newFile.setBinaryContent(javaCode.getBytes(StandardCharsets.UTF_8));
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
            if (line.startsWith("public class") || line.startsWith("public interface")) {
                String[] parts = line.split("\\s+");
                if (parts.length > 2) {
                    return parts[2];
                }
            }
        }
        return null;
    }

    /**
     * 从 XML 内容中提取 .mapper. 之后的类名
     *
     * @param xmlContent XML 内容
     * @return 提取的类名，如果无法提取则返回 null
     */
    private String extractClassNameFromXML(String xmlContent) {
        // 定义正则表达式来匹配 .mapper. 后的类名
        Pattern pattern = Pattern.compile("namespace=\"[^\"]*\\.mapper\\.(\\w+)Mapper\"");
        Matcher matcher = pattern.matcher(xmlContent);
        if (matcher.find()) {
            return matcher.group(1) + "Mapper";
        }
        return null;
    }

    /**
     * 自定义对话框类，用于选择生成的代码类型
     */
    static class CodeGenerationDialog extends DialogWrapper {

        private JCheckBox controllerCheckBox;
        private JCheckBox readmeCheckBox;
        private JCheckBox modelCheckBox;
        private JCheckBox dtoCheckBox;
        private JCheckBox voCheckBox;
        private JCheckBox mapperCheckBox;
        private JCheckBox mapperXmlCheckBox;
        private JCheckBox serviceCheckBox;
        private final Map<String, Boolean> selectedOptions = new HashMap<>();

        protected CodeGenerationDialog(@Nullable Project project) {
            super(project);
            setTitle("选择要生成的代码类型");
            init();
        }

        @Override
        protected JComponent createCenterPanel() {
            // 创建选项卡面板
            JTabbedPane tabbedPane = new JTabbedPane();

            // 创建 Controller 选项卡面板
            JPanel controllerPanel = new JPanel();
            controllerPanel.setLayout(new BoxLayout(controllerPanel, BoxLayout.Y_AXIS));
            controllerPanel.setPreferredSize(new Dimension(600, 450));
            controllerCheckBox = new JCheckBox("Controller");
            mapperCheckBox = new JCheckBox("Mapper");
            serviceCheckBox = new JCheckBox("Service");
            controllerPanel.add(controllerCheckBox);
            controllerPanel.add(mapperCheckBox);
            controllerPanel.add(serviceCheckBox);

            // 创建 Model 选项卡面板，包括 DTO 选项
            JPanel modelPanel = new JPanel();
            modelPanel.setLayout(new BoxLayout(modelPanel, BoxLayout.Y_AXIS));
            modelPanel.setPreferredSize(new Dimension(600, 450));
            modelCheckBox = new JCheckBox("Model");
            dtoCheckBox = new JCheckBox("DTO");
            voCheckBox = new JCheckBox("VO");
            modelPanel.add(modelCheckBox);
            modelPanel.add(dtoCheckBox);
            modelPanel.add(voCheckBox);

            // 创建 README 选项卡面板
            JPanel readmePanel = new JPanel();
            readmePanel.setLayout(new BoxLayout(readmePanel, BoxLayout.Y_AXIS));
            readmePanel.setPreferredSize(new Dimension(600, 450));
            readmeCheckBox = new JCheckBox("README");
            readmePanel.add(readmeCheckBox);

            // 创建 配置 选项卡面板
            JPanel configPanel = new JPanel();
            configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
            configPanel.setPreferredSize(new Dimension(600, 450));
            mapperXmlCheckBox = new JCheckBox("Mapper.xml（MyBatisPlus-3）");
            configPanel.add(mapperXmlCheckBox);

            // 创建 "我直接一把梭哈" 按钮
            JButton selectAllButton = new JButton("我全都要！！！");
            selectAllButton.addActionListener(e -> {
                controllerCheckBox.setSelected(true);
                modelCheckBox.setSelected(true);
                dtoCheckBox.setSelected(true);
                readmeCheckBox.setSelected(true);
                voCheckBox.setSelected(true);
                mapperCheckBox.setSelected(true);
                mapperXmlCheckBox.setSelected(true);
                serviceCheckBox.setSelected(true);
            });

            // 创建一个面板来包含选项卡和按钮
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(tabbedPane, BorderLayout.CENTER);
            mainPanel.add(selectAllButton, BorderLayout.SOUTH);

            // 将面板添加到选项卡中
            tabbedPane.addTab("逻辑相关", controllerPanel);
            tabbedPane.addTab("实体类相关", modelPanel);
            tabbedPane.addTab("配置相关", configPanel);
            tabbedPane.addTab("插件自述", readmePanel);

            return mainPanel;
        }

        @Override
        protected JComponent createSouthPanel() {
            JPanel southPanel = (JPanel) super.createSouthPanel();

            // 创建 帮助 按钮
            JButton helpButton = new JButton("帮助❓");
            helpButton.addActionListener(e -> showHelpDialog());
            southPanel.add(helpButton, BorderLayout.WEST);

            return southPanel;
        }

        private void showHelpDialog() {
            String message = "生成的代码需要根据自己的实际需求进行重构，例如 mapper.xml 中的 type 路径等。\n" +
                    "建议选择拖动重构。";
            Messages.showMessageDialog(message, "帮助", Messages.getInformationIcon());
        }

        @Override
        protected void doOKAction() {
            selectedOptions.put("controller", controllerCheckBox.isSelected());
            selectedOptions.put("model", modelCheckBox.isSelected());
            selectedOptions.put("dto", dtoCheckBox.isSelected());
            selectedOptions.put("readme", readmeCheckBox.isSelected());
            selectedOptions.put("vo", voCheckBox.isSelected());
            selectedOptions.put("mapper", mapperCheckBox.isSelected());
            selectedOptions.put("mapperXml", mapperXmlCheckBox.isSelected());
            selectedOptions.put("service", serviceCheckBox.isSelected());
            super.doOKAction();
        }

        public Map<String, Boolean> getSelectedOptions() {
            return selectedOptions;
        }
    }
}