package io.sajensen.specmate

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem

class GoToTestAction : AnAction("Test") {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val currentFile = e.getData(com.intellij.openapi.actionSystem.CommonDataKeys.VIRTUAL_FILE)

        if (project != null && currentFile != null) {
            val testFilePath = currentFile.path.replaceFirst("src", "spec").replace(".clj", "_spec.clj")
            val testFile = LocalFileSystem.getInstance().findFileByPath(testFilePath)

            if (testFile != null) {
                FileEditorManager.getInstance(project).openFile(testFile, true)
            } else {
                Messages.showMessageDialog(
                        project,
                        "Test file not found: $testFilePath",
                        "File Not Found",
                        Messages.getInformationIcon()
                )
            }
        }
    }
}