/* 
* UART.cpp
*
* Created: 06.11.2013 11:56:37
* Author: U_M00Y1
*/


#include "UART.h"
#include "RingBuffer.h"
#include <string.h>

/**
Объявление входного буфера для UART
*/
CRingBuffer UART::receiver;

/** INT0_vect
Прерывание о том, что пришли данные
*/
ISR(USART_RXC_vect) {

    UART::receiver.writeByte(UDR);
}

/**
Инициализировать UART
*/
void UART::init(void) {

    // задаем скорость UART
    UBRRH = HI(BAUDRATE_DIVIDER);
    UBRRL = LO(BAUDRATE_DIVIDER);
    UCSRA = 0;

    // включаем трансмиттер и ресивер
    UCSRB = 1 << TXEN | 1 << RXEN | 1 << RXCIE;

    // контроль четности: нет
    // стоп бит: 1
    // размер кадра 8 бит
    UCSRC = (1 << URSEL) | (1 << UCSZ0) | (1 << UCSZ1);

    receiver.init();
}

/**
Отправить байт по UART
*/
void UART::sendByte(uint8_t c) {

    while ((UCSRA & (1 << UDRE)) == 0);
    UDR = c;
}

/**
Отправить строку в UART.
Условием корректной работы этой функции является то, что в качестве str использовать только ANSI строку
*/
void UART::send(char* str) {

    uint8_t size = strlen(str);
    sendBuffer((uint8_t*)str, size);
}

/**
Отправить буфер в UART
*/
void UART::sendBuffer(uint8_t* buffer, int size) {

    for (int i=0; i<size; i++) {
        sendByte(buffer[i]);
    }
}
