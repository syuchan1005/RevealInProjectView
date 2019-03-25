package com.github.syuchan1005.revealinprojectview;

import com.intellij.ide.FileEditorProvider;
import com.intellij.ide.SelectInContext;
import com.intellij.ide.SelectInTarget;
import com.intellij.ide.actions.ShowFilePathAction;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.ide.projectView.impl.ProjectViewPane;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;

public class RevealFileAction extends DumbAwareAction {
    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) return;
        ProjectView projectView = ProjectView.getInstance(project);
        VirtualFile file = ShowFilePathAction.findLocalFile(CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext()));
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(!projectView.getCurrentViewId().equals(ProjectViewPane.ID) && (file != null && !file.isDirectory()));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        VirtualFile virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE);
        if (project == null || virtualFile == null) return;
        PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
        if (psiFile == null) return;

        ProjectView projectView = ProjectView.getInstance(project);
        projectView.changeView(ProjectViewPane.ID);
        AbstractProjectViewPane currentProjectViewPane = projectView.getCurrentProjectViewPane();
        SelectInTarget selectInTarget = currentProjectViewPane.createSelectInTarget();
        selectInTarget.selectIn(new MySelectInContext(psiFile, project), false);
    }

    public static class MySelectInContext implements SelectInContext {
        private final PsiFile myPsiFile;
        private final Project myProject;

        public MySelectInContext(@NotNull PsiFile psiFile, @NotNull Project myProject) {
            myPsiFile = psiFile;
            this.myProject = myProject;
        }

        @Override
        @NotNull
        public Project getProject() {
            return myProject;
        }

        @NotNull
        private PsiFile getPsiFile() {
            return myPsiFile;
        }

        @Override
        @NotNull
        public FileEditorProvider getFileEditorProvider() {
            return null;
        }

        @NotNull
        private PsiElement getPsiElement() {
            return myPsiFile;
        }

        @Override
        @NotNull
        public VirtualFile getVirtualFile() {
            return getPsiFile().getVirtualFile();
        }

        @Override
        public Object getSelectorInFile() {
            return getPsiElement();
        }
    }
}
