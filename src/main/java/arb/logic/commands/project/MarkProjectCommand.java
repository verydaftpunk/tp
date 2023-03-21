package arb.logic.commands.project;

import static arb.model.Model.PREDICATE_SHOW_ALL_PROJECTS;
import static arb.model.Model.PROJECT_NO_COMPARATOR;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import arb.commons.core.Messages;
import arb.commons.core.index.Index;
import arb.logic.commands.Command;
import arb.logic.commands.CommandResult;
import arb.logic.commands.exceptions.CommandException;
import arb.model.ListType;
import arb.model.Model;
import arb.model.project.Project;

/**
 * Marks a project identified using its displayed index from the address book.
 */
public class MarkProjectCommand extends Command {

    public static final String MESSAGE_MARK_PROJECT_SUCCESS = "Marked Project: %1$s";

    private static final String MAIN_COMMAND_WORD = "mark";
    private static final String ALIAS_COMMAND_WORD = "mp";
    private static final Set<String> COMMAND_WORDS =
            new HashSet<>(Arrays.asList(MAIN_COMMAND_WORD, ALIAS_COMMAND_WORD));

    public static final String MESSAGE_USAGE = MAIN_COMMAND_WORD
            + ": Marks the project identified by the index number used in the displayed project list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + MAIN_COMMAND_WORD + " 1";

    private final Index targetIndex;

    public MarkProjectCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, ListType currentListBeingShown) throws CommandException {
        requireNonNull(model);
        List<Project> lastShownList = model.getSortedProjectList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PROJECT_DISPLAYED_INDEX);
        }

        if (currentListBeingShown != ListType.PROJECT) {
            throw new CommandException(Messages.MESSAGE_INVALID_LIST_PROJECT);
        }

        Project projectToMark = lastShownList.get(targetIndex.getZeroBased());
        projectToMark.markAsDone();

        model.setProject(projectToMark, projectToMark);
        model.updateFilteredProjectList(PREDICATE_SHOW_ALL_PROJECTS);
        model.updateSortedProjectList(PROJECT_NO_COMPARATOR);
        return new CommandResult(String.format(MESSAGE_MARK_PROJECT_SUCCESS, projectToMark), ListType.PROJECT);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MarkProjectCommand // instanceof handles nulls
                && targetIndex.equals(((MarkProjectCommand) other).targetIndex)); // state check
    }

    public static boolean isCommandWord(String commandWord) {
        return COMMAND_WORDS.contains(commandWord);
    }

    public static List<String> getCommandWords() {
        return new ArrayList<>(COMMAND_WORDS);
    }
}
