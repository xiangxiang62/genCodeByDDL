package main.java.com.xiangxiang.genCodeByDDL.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;
import main.java.com.xiangxiang.genCodeByDDL.builder.TableSchemaBuilder;
import main.java.com.xiangxiang.genCodeByDDL.model.GenerateBySQLVO;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 主启动类
 *
 * @author 香香
 */
public class GenCodeByDDLAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject(); // 获取当前项目
        if (project == null) {
            Messages.showMessageDialog("找不到项目。", "错误", Messages.getErrorIcon());
            return;
        }

        VirtualFile file = event.getDataContext().getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE);
        if (file != null && "sql".equals(file.getExtension())) {
            try {
                // 读取文件内容
                String fileContent = new String(file.contentsToByteArray(), StandardCharsets.UTF_8);

                // 根据 SQL 生成代码
                GenerateBySQLVO generateBySQLVO = TableSchemaBuilder.buildFromDDL(fileContent);
                List<String> javaControllerCode = generateBySQLVO.getJavaControllerCode();
                List<String> javaAddEntityCode = generateBySQLVO.getJavaAddEntityCode();
                List<String> javaEntityCodeList = generateBySQLVO.getJavaEntityCode();

                // 执行写操作的代码
                WriteCommandAction.runWriteCommandAction(project, () -> {
                    try {
                        VirtualFile projectRoot = getProjectRoot(project);
                        if (projectRoot == null) return;

                        // 创建 `generator` 目录
                        VirtualFile generatorDir = getGeneratorDir(projectRoot, "generator");
                        // 创建 `model` 目录
                        createModelToEntity(javaEntityCodeList, generatorDir, "model");
                        // 创建 `controller` 目录
                        createModelToEntity(javaControllerCode, generatorDir, "controller");
                        createModelToEntity(javaAddEntityCode, generatorDir, "dto");

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

    @Nullable
    private VirtualFile getProjectRoot(Project project) {
        // 获取项目的根目录
        VirtualFile projectRoot = project.getBaseDir();
        if (projectRoot == null) {
            Messages.showMessageDialog("找不到项目根目录。", "错误", Messages.getErrorIcon());
            return null;
        }
        return projectRoot;
    }

    @NotNull
    private VirtualFile getGeneratorDir(VirtualFile projectRoot, String generator) throws IOException {
        // 获取 `generator` 目录
        VirtualFile generatorDir = projectRoot.findChild(generator);
        if (generatorDir == null) {
            // 如果 `generator` 目录不存在，则创建它
            generatorDir = projectRoot.createChildDirectory(this, generator);
        }
        return generatorDir;
    }

    private void createModelToEntity(List<String> javaEntityCodeList, VirtualFile generatorDir, String model) throws IOException {
        VirtualFile modelDir = getGeneratorDir(generatorDir, model);

        // 将每个 Java 实体代码写入对应的文件
        for (String javaEntityCode : javaEntityCodeList) {
            // 提取类名作为文件名
            String className = extractClassName(javaEntityCode);
            if (className != null) {
                VirtualFile newFile = modelDir.createChildData(this, className + ".java");
                newFile.setBinaryContent(javaEntityCode.getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    private String extractClassName(String javaEntityCode) {
        // 假设类名在 `public class` 之后的第一个单词
        String[] lines = javaEntityCode.split("\\n");
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
}
