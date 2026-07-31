Feature: Task crud and Editing
  As a user
  I want to create, update, and delete my tasks
  So that I can keep my daily workflow up to date

  Scenario: Create a new task with default status
    Given the task system is ready
    When I create a task titled "Submit Internship Assignment" with priority "HIGH"
    Then the task should be saved successfully
    And the task status should automatically be "TODO"

  Scenario: Move a task from TODO to IN_PROGRESS
    Given an existing task titled "Write Backend Tests" with status "TODO"
    When I change the task status to "IN_PROGRESS"
    Then the task status should be updated to "IN_PROGRESS"

  Scenario: Increase task priority when deadline is close
    Given an existing task titled "Fix Database Config" with priority "LOW"
    When I change the task priority to "HIGH"
    Then the task priority should be updated to "HIGH"

  Scenario: Delete a task after completion
    Given an existing task titled "Old Setup Task" exists in my list
    When I delete the task titled "Old Setup Task"
    Then the task titled "Old Setup Task" should no longer exist in my list