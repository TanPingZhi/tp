package seedu.nutrigoals.logic.commands;

import static seedu.nutrigoals.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.nutrigoals.logic.commands.CommandTestUtil.showFoodAtIndex;
import static seedu.nutrigoals.testutil.FoodBuilder.DEFAULT_EARLIER_TIME;
import static seedu.nutrigoals.testutil.TypicalFoods.getTypicalNutriGoals;
import static seedu.nutrigoals.testutil.TypicalIndexes.INDEX_FIRST_MEAL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.nutrigoals.model.Model;
import seedu.nutrigoals.model.ModelManager;
import seedu.nutrigoals.model.UserPrefs;
import seedu.nutrigoals.model.meal.DateTime;
import seedu.nutrigoals.model.meal.Food;
import seedu.nutrigoals.model.meal.IsFoodAddedOnThisDatePredicate;
import seedu.nutrigoals.testutil.FoodBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalNutriGoals(), new UserPrefs());
        expectedModel = new ModelManager(model.getNutriGoals(), new UserPrefs());
    }

    @Test
    public void execute_listIsNotFiltered_showsAllFoodsAddedToday() {
        DateTime today = new DateTime();
        IsFoodAddedOnThisDatePredicate isTodayPredicate = new IsFoodAddedOnThisDatePredicate(today);
        ListCommand listCommand = new ListCommand(isTodayPredicate);
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, today.getDate());

        assertCommandSuccess(listCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listIsFilteredForToday_showsAllFoodsAddedToday() {
        DateTime today = new DateTime();
        IsFoodAddedOnThisDatePredicate isTodayPredicate = new IsFoodAddedOnThisDatePredicate(today);
        ListCommand listCommand = new ListCommand(isTodayPredicate);
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, today.getDate());

        showFoodAtIndex(model, INDEX_FIRST_MEAL);
        assertCommandSuccess(listCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_listIsFilteredForADay_showsAllFoodsAddedOnThatDay() {
        Food foodToAdd = new FoodBuilder().withDateTime(DEFAULT_EARLIER_TIME).build();
        model.addFood(foodToAdd);
        expectedModel.addFood(foodToAdd);

        DateTime date = new DateTime(DEFAULT_EARLIER_TIME);
        IsFoodAddedOnThisDatePredicate datePredicate = new IsFoodAddedOnThisDatePredicate(date);
        ListCommand listCommand = new ListCommand(datePredicate);
        String expectedMessage = String.format(ListCommand.MESSAGE_SUCCESS, date.getDate());

        expectedModel.updateFilteredFoodList(datePredicate);
        assertCommandSuccess(listCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredListIsEmpty_showsNoFoods() {
        DateTime date = new DateTime(DEFAULT_EARLIER_TIME);
        IsFoodAddedOnThisDatePredicate datePredicate = new IsFoodAddedOnThisDatePredicate(date);
        ListCommand listCommand = new ListCommand(datePredicate);
        String expectedMessage = String.format(ListCommand.MESSAGE_EMPTY_FOOD_LIST, date.getDate());

        expectedModel.updateFilteredFoodList(datePredicate);
        assertCommandSuccess(listCommand, model, expectedMessage, expectedModel);
    }
}
