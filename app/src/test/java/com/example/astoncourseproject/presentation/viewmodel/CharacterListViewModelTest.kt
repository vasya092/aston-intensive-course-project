package com.example.astoncourseproject.presentation.viewmodel

import com.example.astoncourseproject.data.repositories.CharacterRepositoryImpl
import com.example.astoncourseproject.presentation.models.*
import io.kotest.assertions.AssertionFailedError
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest : BehaviorSpec({

    val dispatcher = StandardTestDispatcher()
    Dispatchers.setMain(dispatcher)

    isolationMode = IsolationMode.InstancePerLeaf

    val repository = mockk<CharacterRepositoryImpl>(relaxed = true)
    val viewModel = CharacterListViewModel(repository)
    Given("test onUpdateSearchField") {
        val response = "test field"
        When("Method is called") {
            viewModel.onUpdateSearchField("test field")
            Then("search field should be a test field") {
                viewModel.searchField.value shouldBe response
            }
        }
    }

    Given("test onClearNameField") {
        val response = ""
        When("Method is called") {
            viewModel.onClearSearchField()
            Then("search field should be a test field") {
                viewModel.searchField.value shouldBe response
            }
        }
    }

    Given("test onFindByNameWithoutFilter") {
        val name = "Rick"
        val expectedStatus = Success
        When("items flow updated with page 1") {
            viewModel.onFindByNameWithoutFilter(name)
            Then("should be update status as success") {
                viewModel.status.value shouldBe expectedStatus
            }
        }
        When("items flow updated with page 2") {
            viewModel.onFindByNameWithoutFilter(name, 2)
            Then("should be update status as success") {
                viewModel.status.value shouldBe expectedStatus
            }
        }
    }

    Given("test onFindByNameWithoutFilter with failure") {
        val exceptedStatus = Failure
        When("items flow updated") {
            viewModel.onFindByNameWithoutFilter("Rick")
            Then("should be update status as failure") {
                assertThrows<AssertionFailedError> {
                    viewModel.status.value shouldBe exceptedStatus
                }
            }
        }
    }
})
