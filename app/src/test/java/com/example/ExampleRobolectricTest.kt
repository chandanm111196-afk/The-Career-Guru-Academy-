package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("The Career Guru Academy", appName)
  }

  @Test
  fun `verify academy seed data contacts and branches`() {
    val profile = com.example.data.local.SeedData.initialProfile
    assertEquals("9093200422", profile.mobile)
    assertEquals("Suri, Birbhum", profile.branch)
    assertTrue(com.example.data.local.SeedData.mockTests.isNotEmpty())
    assertTrue(com.example.data.local.SeedData.liveClasses.isNotEmpty())
  }
}
