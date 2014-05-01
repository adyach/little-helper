/* 
* UART.h
*
* Created: 06.11.2013 11:56:37
* Author: U_M00Y1
*/


#ifndef __UART_H__
#define __UART_H__

#include "stdafx.h"
#include "RingBuffer.h"

// настройки UART
#define XTAL F_CPU

#ifndef BAUDRATE
#define BAUDRATE 9600L
#endif

#define BAUDRATE_DIVIDER (XTAL / (16 * BAUDRATE) - 1)

/**
Реализация работы с UART
*/
class UART
{
public:
    static CRingBuffer receiver;

public:
    static void init(void);
    static void sendByte(uint8_t c);
    static void send(char* str);
    static void sendBuffer(uint8_t* str, int size);
}; //UART

#endif //__UART_H__
