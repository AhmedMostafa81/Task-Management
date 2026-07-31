Feature: Task Filtering and Searching
  As a user
  I want to filter my tasks by status and priority
  So that I can focus on what matters most right now

  Background:
    Given my task list contains the following tasks:
      | Title                 | Status      | Priority |
      | Setup Spring Boot     | DONE        | HIGH     |
      | Write BDD Scenarios   | IN_PROGRESS | HIGH     |
      | Design Angular UI     | TODO        | MEDIUM   |
      | Refactor Entity Files | TODO        | LOW      |

  Scenario: Filter tasks to see only completed work
    When I filter my tasks by status "DONE"
    Then I should see 1 task in the results

  Scenario: Filter tasks to see all high priority work
    When I filter my tasks by priority "HIGH"
    Then I should see 2 tasks in the results

  Scenario: Filter by a status that has no matching tasks yet
    Given my task list contains only "TODO" tasks
    When I filter my tasks by status "DONE"
    Then I should see 0 tasks in the results