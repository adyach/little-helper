/*
 * SimpleTimer.h
 *
 *  Created on: 26 нояб. 2013 г.
 *      Author: U_M00Y1
 */

#ifndef SIMPLETIMER_H_
#define SIMPLETIMER_H_

#include "stdafx.h"

#define TIMERS_COUNT 2
#define TICK_INTERVAL_MS   1  // Tick period 100ms
#define TICKS_PER_SEC (1000 / TICK_INTERVAL_MS)

class SimpleTimer {
private:
	uint8_t flag;
	uint32_t timers[TIMERS_COUNT];

public:
	static SimpleTimer* instance;

public:
	SimpleTimer();
	void start(uint8_t index, uint32_t tickCount);
	void onTick(void);
	uint8_t getFlag(void);
};

#endif /* SIMPLETIMER_H_ */
