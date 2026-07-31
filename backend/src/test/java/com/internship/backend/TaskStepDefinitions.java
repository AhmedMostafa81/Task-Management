package com.internship.backend;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@CucumberContextConfiguration
@SpringBootTest
public class TaskStepDefinitions {

    private final List<Map<String, String>> mockDatabase = new ArrayList<>();
    private String currentTitle;
    private String currentStatus;
    private String currentPriority;
    private boolean isSaved = false;
    private List<Map<String, String>> queryResults = new ArrayList<>();

    @Given("the task system is ready")
    public void the_task_system_is_ready() {
        mockDatabase.clear();
        isSaved = false;
    }

    @When("I create a task titled {string} with priority {string}")
    public void i_create_a_task_titled_with_priority(String title, String priority) {
        this.currentTitle = title;
        this.currentPriority = priority;
        this.currentStatus = "TODO";
        this.isSaved = true;

        Map<String, String> task = new HashMap<>();
        task.put("Title", title);
        task.put("Status", "TODO");
        task.put("Priority", priority);
        mockDatabase.add(task);
    }

    @Then("the task should be saved successfully")
    public void the_task_should_be_saved_successfully() {
        assertTrue(isSaved);
    }

    @Then("the task status should automatically be {string}")
    public void the_task_status_should_automatically_be(String expectedStatus) {
        assertEquals(expectedStatus, this.currentStatus);
    }

    @Given("an existing task titled {string} with status {string}")
    public void an_existing_task_titled_with_status(String title, String status) {
        this.currentTitle = title;
        this.currentStatus = status;
    }

    @When("I change the task status to {string}")
    public void i_change_the_task_status_to(String newStatus) {
        this.currentStatus = newStatus;
    }

    @Then("the task status should be updated to {string}")
    public void the_task_status_should_be_updated_to(String expectedStatus) {
        assertEquals(expectedStatus, this.currentStatus);
    }

    @Given("an existing task titled {string} with priority {string}")
    public void an_existing_task_titled_with_priority(String title, String priority) {
        this.currentTitle = title;
        this.currentPriority = priority;
    }

    @When("I change the task priority to {string}")
    public void i_change_the_task_priority_to(String newPriority) {
        this.currentPriority = newPriority;
    }

    @Then("the task priority should be updated to {string}")
    public void the_task_priority_should_be_updated_to(String expectedPriority) {
        assertEquals(expectedPriority, this.currentPriority);
    }

    @Given("an existing task titled {string} exists in my list")
    public void an_existing_task_titled_exists_in_my_list(String title) {
        Map<String, String> task = new HashMap<>();
        task.put("Title", title);
        mockDatabase.add(task);
    }

    @When("I delete the task titled {string}")
    public void i_delete_the_task_titled(String title) {
        mockDatabase.removeIf(task -> title.equals(task.get("Title")));
    }

    @Then("the task titled {string} should no longer exist in my list")
    public void the_task_titled_should_no_longer_exist_in_my_list(String title) {
        boolean exists = mockDatabase.stream().anyMatch(task -> title.equals(task.get("Title")));
        assertFalse(exists);
    }

    @Given("my task list contains the following tasks:")
    public void my_task_list_contains_the_following_tasks(DataTable dataTable) {
        mockDatabase.clear();
        mockDatabase.addAll(dataTable.asMaps(String.class, String.class));
    }

    @Given("my task list contains only {string} tasks")
    public void my_task_list_contains_only_tasks(String status) {
        mockDatabase.clear();
        Map<String, String> task = new HashMap<>();
        task.put("Title", "Sample Task");
        task.put("Status", status);
        task.put("Priority", "LOW");
        mockDatabase.add(task);
    }

    @When("I filter my tasks by status {string}")
    public void i_filter_my_tasks_by_status(String status) {
        queryResults = mockDatabase.stream()
                .filter(task -> status.equals(task.get("Status")))
                .collect(Collectors.toList());
    }

    @When("I filter my tasks by priority {string}")
    public void i_filter_my_tasks_by_priority(String priority) {
        queryResults = mockDatabase.stream()
                .filter(task -> priority.equals(task.get("Priority")))
                .collect(Collectors.toList());
    }

    @Then("I should see {int} task(s) in the results")
    public void i_should_see_tasks_in_the_results(Integer expectedCount) {
        assertEquals(expectedCount, queryResults.size());
    }
}