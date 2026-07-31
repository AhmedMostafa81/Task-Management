Feature: Task Lifecycle and Editing
  As a user
  I want to create, update, and delete my tasks
  So that I can keep my daily workflow up to date

  Scenario: Create a new task
    Given the task system is ready
    When I create a task titled "Submit Internship Assignment" with priority "HIGH" and status "TODO"
    Then the task should be saved successfully with a generated ID
    And the task status should be "TODO"

  Scenario: Move a task from TODO to IN_PROGRESS
    Given I have an existing task titled "Write Backend Tests" with status "TODO" and priority "MEDIUM"
    When I change the task status by its ID to "IN_PROGRESS"
    Then the task status should be updated to "IN_PROGRESS"

  Scenario: Increase task priority when deadline is close
    Given I have an existing task titled "Fix Database Config" with status "TODO" and priority "LOW"
    When I change the task priority by its ID to "HIGH"
    Then the task priority should be updated to "HIGH"

  Scenario: Delete a task after completion
    Given I have an existing task titled "Old Setup Task" with status "DONE" and priority "LOW"
    When I delete the task by its ID
    Then the task should no longer exist in the database