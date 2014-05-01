/*
 * SimpleTimer.cpp
 *
 *  Created on: 26 нояб. 2013 г.
 *      Author: U_M00Y1
 */

#include "SimpleTimer.h"
#include "util/atomic.h"

SimpleTimer* SimpleTimer::instance = NULL;

/**
 * Timer0 Overflow interrupt vector
 */
ISR(TIMER1_COMPA_vect) {

	if (SimpleTimer::instance) {
		SimpleTimer::instance->onTick();
	}
}

/**
 * Default constructor
 */
SimpleTimer::SimpleTimer() {

	instance = this;
	flag = 0;

    // init timer 1
    TCCR1A = 0;
    TCCR1B = 1 << WGM12 | 1 << CS11 | 1 << CS10; // 1/64 prescaler, CTC-mode
    OCR1A = 124; // 125
}

/**
 * Synchronized Function.
 * Start simple timer.
 */
void SimpleTimer::start(uint8_t index, uint32_t tickCount) {

	ATOMIC_BLOCK(ATOMIC_RESTORESTATE) {
		timers[index] = tickCount;
		flag &= ~(1 << index);
	}

	// Start Timer/Counter 1
    TIMSK |= 1 << OCIE1A;
}

/**
 * Update software counters.
 * This function invokes always from interrupt.
 */
void SimpleTimer::onTick(void) {

	for (uint8_t i=0; i<TIMERS_COUNT; i++) {
		if (timers[i] != 0 && --timers[i] == 0) {
			flag |= 1 << i;
		}
	}
}

/**
 * Synchronized Function.
 * Atomically returns flag state.
 */
uint8_t SimpleTimer::getFlag(void) {

	ATOMIC_BLOCK(ATOMIC_RESTORESTATE) {
		return flag;
	}
}
