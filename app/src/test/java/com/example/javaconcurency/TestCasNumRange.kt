package com.example.javaconcurency

import org.amshove.kluent.shouldBeGreaterThan
import org.amshove.kluent.shouldBeLessThan
import org.junit.Test

class TestCasNumRange {
    private val casNumRange = CasNumberRange()
    @Test
    fun `upper greater than lower`() {
        casNumRange.upper = 3 shouldBeGreaterThan casNumRange.lower
    }

    @Test
    fun `lower is less than upper`() {
        casNumRange.upper = 3
        casNumRange.lower = 2 shouldBeLessThan casNumRange.upper
    }
}