package arb.ui;

import java.util.logging.Logger;

import arb.commons.core.LogsCenter;
import arb.model.ProjectStub;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;

/**
 * Panel containing the list of persons.
 */
public class ProjectListPanel extends UiPart<Region> {
    private static final String FXML = "ProjectListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(ProjectListPanel.class);

    @FXML
    private ListView<ProjectStub> projectListView;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public ProjectListPanel(ObservableList<ProjectStub> projectList) {
        super(FXML);
        projectListView.setItems(projectList);
        projectListView.setCellFactory(listView -> new ProjectListViewCell());
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code PersonCard}.
     */
    class ProjectListViewCell extends ListCell<ProjectStub> {
        @Override
        protected void updateItem(ProjectStub project, boolean empty) {
            super.updateItem(project, empty);

            if (empty || project == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ProjectCard(project, getIndex() + 1).getRoot());
            }
        }
    }

}
