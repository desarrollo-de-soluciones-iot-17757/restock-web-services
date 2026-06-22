# language: en
# Create Conciliation Task feature

Feature: Create Conciliation Task
  As a system user
  I want to create a conciliation task
  So that inventory discrepancies are resolved

  Scenario: Successful creation of a conciliation task
    Given a device with ID "device-123" exists
    And a batch with ID "batch-123" exists
    When I send a request to create a conciliation task
    Then the task is created successfully
    And the response status is 201
