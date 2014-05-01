/*
 * CmdProcessor.h
 *
 * Command fromat:
 *
 * [cmd][data]
 * cmd - 1 byte
 * data - 4 bytes
 *
 * 0x01 0x00 0x00 0x00 0x00 0x00
 *
 *  Created on: Apr 13, 2014
 *      Author: andrey
 */

#ifndef CMDPROCESSOR_H_
#define CMDPROCESSOR_H_
//
#include "stdafx.h"
#include "string.h"
#include "avr/io.h"
#include "util/delay.h"
#include "SimpleTimer.h"
#include "UART.h"
#include "CLed.h"

#define MOVE_FORWARD 0x01
#define MOVE_RIGHT   0x02
#define MOVE_LEFT    0x03
#define MOVE_STOP    0x04
#define HB		 	 0x05

#define OK 		     0x00
#define ERROR        0xFF

#define CMD_POS 	 0
#define DL_POS 	     1
#define DATA_POS 	 2
#define PACKET_SIZE  6

#define MOTO_RIGHT	PB2
#define MOTO_LEFT	PB3

#define HB_INTERVAL_S  10
#define HB_TIMER_INDEX 0

struct Packet {

	uint8_t packet[PACKET_SIZE];

	Packet(void) {
		clear();
	}

	void clear(void) {

		::memset(this, 0, sizeof(Packet));
	}

	void fillPacket(uint8_t* buffer, uint8_t size) {
		memcpy(packet, buffer, size);
	}

	uint8_t getCmd() {
		return packet[CMD_POS];
	}

	uint8_t getDl() {
		return packet[DL_POS];
	}

	uint8_t* getData() {

		uint8_t data[4];
		data[0] = packet[2];
		data[1] = packet[3];
		data[2] = packet[4];
		data[3] = packet[5];

		return data;
	}

	uint8_t* getPacket() {
		return packet;
	}
};

class CmdProcessor {

	SimpleTimer timer;

public:

	CmdProcessor();
	void topLoop();
	void processRequest();
	void detectObstacle();
	void processResponse(Packet* packet);
	void readMessage();
	void processError(Packet* packet);
};

#endif /* CMDPROCESSOR_H_ */
