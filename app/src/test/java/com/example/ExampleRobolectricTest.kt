package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.DataRepository
import com.example.data.LifeSkillId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Kids Life Skills", appName)
  }

  @Test
  fun `repository maintains profile and exports backup`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = DataRepository(context)

    repo.updateProfile(name = "Leo Champion", age = 7, avatarId = "hero_boy")
    assertEquals("Leo Champion", repo.profile.value.name)
    assertEquals(7, repo.profile.value.age)

    repo.completeSkill(LifeSkillId.BRUSH_TEETH, 40, 1)
    assertTrue(repo.completedSkillIds.value.contains(LifeSkillId.BRUSH_TEETH.key))

    val backup = repo.exportBackupJson()
    assertTrue(backup.contains("Leo Champion"))
    assertTrue(backup.contains("brush_teeth"))

    val restoreSuccess = repo.importBackupJson(backup)
    assertTrue(restoreSuccess)
  }
}
