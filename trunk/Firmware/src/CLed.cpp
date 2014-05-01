/*
 * CLed.cpp
 *
 *  Created on: Apr 19, 2014
 *      Author: andrey
 */

#include "CLed.h"

void CLed::initLeds() {

	DDRC = 0x1F; // 0b00011111;
	PORTC = ON << IO_ACTIVITY;
	_delay_ms(3000);
	PORTC = OFF << IO_ACTIVITY;
}

void CLed::turnLed_10ms (uint8_t led) {

	PORTC = ON << led;
	_delay_ms(10);
	PORTC = OFF << led;
}
