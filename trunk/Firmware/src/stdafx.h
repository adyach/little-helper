/*
 * stdafx.h
 *
 * Created: 25.06.2013 16:07:21
 *  Author: U_M00Y1
 */ 

#ifndef STDAFX_H_
#define STDAFX_H_

#include <avr/io.h>
#include <avr/interrupt.h>

#include "setup.h"

#define TRUE 1
#define FALSE 0
#define NULL 0
#define RET_OK 0

#define HIDEC(x) (x >> 4);
#define LODEC(x) (x & 0x0F);

#define HI(x) (x >> 8)
#define LO(x) (x & 0xFF)

typedef void (*TPTR)(void);

#endif /* STDAFX_H_ */
