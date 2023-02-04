package com.example.astoncourseproject.presentation.viewmodel

import com.example.astoncourseproject.domain.models.LocationLinkDomain
import com.example.astoncourseproject.presentation.models.CharacterDetail
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailViewModelTest : BehaviorSpec() {

    val dispatcher = StandardTestDispatcher()
    override fun isolationMode(): IsolationMode = IsolationMode.InstancePerTest

    init {

        Dispatchers.setMain(dispatcher)

        val fakeLocation = mockk<LocationLinkDomain>(relaxed = true)
        val fakeEpisodes = mockk<List<String>>(relaxed = true)
        val id: Long = 1
        val fakeName = "Rick"
        val fakeStatus = "Alive"
        val fakeSpecies = "Human"
        val fakeGender = "Male"
        val fakeType = ""
        val fakeImage = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"

        Given("Character flow tests") {
            val startFlow = MutableStateFlow(
                CharacterDetail(id,
                    fakeName,
                    fakeStatus,
                    fakeSpecies,
                    fakeGender,
                    fakeType,
                    fakeImage,
                    fakeLocation,
                    fakeLocation,
                    fakeEpisodes
                )
            )

            When("We init a viewModel with fake data") {
                val viewModel = mockk<CharacterDetailViewModel>()
                every { viewModel.character } returns startFlow
                Then("character flow after init with fake data") {
                    assertSoftly {
                        with(viewModel.character.value) {
                            id shouldBe id
                            name shouldBe fakeName
                            status shouldBe fakeStatus
                        }
                    }
                }
            }
        }
    }
}
