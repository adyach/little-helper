/*
 * CmdProcessor.cpp
 *
 *  Created on: Apr 13, 2014
 *      Author: andrey
 */

#include "CmdProcessor.h"

CmdProcessor cmdProc;

int main() {

	DDRC = 1 << PC0;
	PORTC = 1 << PC0;
	_delay_ms(2000);
	PORTC = 0;

	UART::init();
	CLed::initLeds();

	sei();

	DDRB = 0b00001110;

	UART::send("READY");

	cmdProc.topLoop();
}

CmdProcessor::CmdProcessor() {

}

void CmdProcessor::topLoop() {

	// init timer
//	timer.start(HB_TIMER_INDEX, HB_INTERVAL_S * TICKS_PER_SEC);

	while (TRUE) {

//		if (timer.getFlag() & (1 << HB_TIMER_INDEX)) {
//			uint8_t buff[3];
//			buff[0] = HB;
//			buff[1] = 1;
//			buff[2] = OK;
//
//			UART::sendBuffer(buff, 3);
//			CLed::turnLed_10ms(IO_ACTIVITY);
//			timer.start(HB_TIMER_INDEX, HB_INTERVAL_S * TICKS_PER_SEC);
//		}

		cmdProc.processRequest();
	}
}

void CmdProcessor::processRequest() {

	const int size = sizeof(Packet);
	if (UART::receiver.available() <= size) {
		return;
	}

	PORTC = 1 << PC0;
	_delay_ms(100);
	PORTC = 0;

	Packet packet;
	UART::receiver.read((uint8_t*) &packet, size);
	CLed::turnLed_10ms(IO_ACTIVITY);

	uint8_t cmd = packet.getCmd();
	switch (cmd) {
	case MOVE_FORWARD:
		PORTB = 1 << MOTO_LEFT | 1 << MOTO_RIGHT;
		break;
	case MOVE_RIGHT:
		PORTB = 0 << MOTO_LEFT | 1 << MOTO_RIGHT;
		break;
	case MOVE_LEFT:
		PORTB = 1 << MOTO_LEFT | 0 << MOTO_RIGHT;
		break;
	case MOVE_STOP:
		PORTB = 0 << MOTO_LEFT | 0 << MOTO_RIGHT;
		break;
	default:
		processError(&packet);
		return;
	}

	processResponse(&packet);
}

void CmdProcessor::detectObstacle() {

}

void CmdProcessor::processResponse(Packet* packet) {

	uint8_t buff[3];
	buff[0] = packet->getCmd();
	buff[1] = 1;
	buff[2] = OK;

	UART::sendBuffer(buff, 3);
	CLed::turnLed_10ms(IO_ACTIVITY);
}

void CmdProcessor::processError(Packet* packet) {

	uint8_t buff[3];
	buff[0] = packet->getCmd();
	buff[1] = 1;
	buff[2] = ERROR;

	UART::sendBuffer(buff, 3);
	CLed::turnLed_10ms(IO_ACTIVITY);
}
