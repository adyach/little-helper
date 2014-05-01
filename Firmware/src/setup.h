/*
 * setup.h
 *
 * Настройки параметров конкретного экземпляра проекта.
 *
 * Created: 25.06.2013 16:50:23
 *  Author: U_M00Y1
 */

#ifndef SETUP_H_
#define SETUP_H_

#define F_CPU 8000000 // тактовая частота процессора
// #define __AVR_ATmega8__ // задаем имя микроконтроллера

// --- UART: задаем параметры
#define USES_TRANSMITTER
#define TRANSMITTER_BUFFER_SIZE 10

// #define USES_RECEIVER
// #define RECEIVER_BUFFER_SIZE 4

//#define USE_LOGGER 1

#endif /* SETUP_H_ */
