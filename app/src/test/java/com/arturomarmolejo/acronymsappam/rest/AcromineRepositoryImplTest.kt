package com.arturomarmolejo.acronymsappam.rest

import androidx.lifecycle.MutableLiveData
import com.arturomarmolejo.acronymsappam.model.AcromineItem
import com.arturomarmolejo.acronymsappam.utils.UIState
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


internal class AcromineRepositoryImplTest {

    private lateinit var testObject: AcromineRepositoryImpl
    private val mockApi = mockk<AcromineServiceApi>(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)



    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testObject = AcromineRepositoryImpl(mockApi)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `get MEANING when server retrieves the LONG FORM from the SHORT FORM and RETURNS A SUCCESS State`() {
        //AAA
        coEvery { mockApi.getAcronymList(sf = "a", lf = null) } returns mockk{
            every { isSuccessful } returns true
            every { body() } returns mutableListOf(mockk(), mockk(), mockk())
        }
        //ACTION
        val job = testScope.launch {
            testObject.getAllAcronyms(lf = "a").collect {
                if (it is UIState.SUCCESS) assertEquals(3, it.response.size)
            }
        }

        job.cancel()
    }

    @Test
    fun `get NULL when server retrieves the LONG FORM from the SHORT FORM and returns an ERROR State`() {
        coEvery { mockApi.getAcronymList("a", null) } returns mockk{
            every { isSuccessful } returns true
            every { body() } returns null
        }
        //action
        val job = testScope.launch {
            testObject.getAllAcronyms().collect{
                if(it is UIState.ERROR) assertEquals("Information not available", it.error)
            }
        }

        job.cancel()
    }

    @Test
    fun `get FAILURE RESPONSE when server retrieves the LONG FORM from the SHORT FORM and returns an ERROR State`() {
        //AAA
        //assign
        coEvery { mockApi.getAcronymList("a", null) } returns mockk{
            every { isSuccessful } returns false
            every { errorBody() } returns mockk {
                every { string() } returns "ERROR"
            }
        }
        val job = testScope.launch {
            testObject.getAllAcronyms().collect{
                if(it is UIState.ERROR) assertEquals("Information not available", it.error)
            }
        }

        job.cancel()
    }



}