package com.arturomarmolejo.acronymsappam.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.arturomarmolejo.acronymsappam.rest.AcromineRepository
import com.arturomarmolejo.acronymsappam.utils.UIState
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


internal class AcronymsViewModelTest {

    @get:Rule val rule = InstantTaskExecutorRule()

    private lateinit var testObject: AcronymsViewModel

    private val mockRepository = mockk<AcromineRepository>(relaxed = true)
    private val mockDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(mockDispatcher)
        testObject = AcronymsViewModel(mockRepository, mockDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `getAllAcronyms GET ACRONYMS when repository returns SUCCESS state`() {
        every { mockRepository.getAllAcronyms() } returns flowOf(
            UIState.SUCCESS(mutableListOf(mockk(), mockk(), mockk()))
        )
        testObject.longFormList.observeForever{
            if(it is UIState.SUCCESS) {
                assertEquals(3, it.response.size)
            }
        }

        testObject.getAllAcronyms()
    }

    @Test
    fun `getAllAcronyms GET FAILURE RESPONSE when repository returns SUCCESS state`() {
        every { mockRepository.getAllAcronyms() } returns flowOf(
            UIState.ERROR(Exception("Error"))
        )
        testObject.longFormList.observeForever{
            if(it is UIState.ERROR) {
                assertEquals("Error", it.error)
            }
        }

        testObject.getAllAcronyms()
    }


}