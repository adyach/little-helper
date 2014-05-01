/*
 * CLed.h
 *
 *  Created on: Apr 19, 2014
 *      Author: andrey
 */

#ifndef CLED_H_
#define CLED_H_

#include "avr/io.h"
#include "util/delay.h"

#define IO_ACTIVITY PC0
#define LED_1 PC1
#define LED_2 PC2
#define LED_3 PC3
#define LED_4 PC4

#define ON 1
#define OFF 0

class CLed {

public:
	static void initLeds();
	static void turnLed_10ms (uint8_t led);
};

#endif /* CLED_H_ */
