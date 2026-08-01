Feature: Task Filtering and Searching
  As a user
  I want to filter my tasks by status and priority
  So that I can focus on what matters most right now

  Background:
    Given my task list contains the following tasks:
      | Title                 | Status      | Priority | User     |
      | Setup Spring Boot     | DONE        | HIGH     | testuser |
      | Write BDD Scenarios   | IN_PROGRESS | HIGH     | testuser |
      | Design Angular UI     | TODO        | MEDIUM   | testuser |
      | Refactor Entity Files | TODO        | LOW      | testuser |

  Scenario: Filter tasks to see only completed work
    When I filter my tasks by status "DONE"
    Then I should see 1 task in the results

  Scenario: Filter tasks to see all high priority work
    When I filter my tasks by priority "HIGH"
    Then I should see 2 tasks in the results

  Scenario: Filter by a status that has no matching tasks yet
    Given my task list contains only "TODO" tasks for user "testuser"
    When I filter my tasks by status "DONE"
    Then I should see 0 tasks in the results

  Scenario: Filter tasks by both status and priority
    When I filter my tasks by status "TODO" and priority "MEDIUM"
    Then I should see 1 task in the results