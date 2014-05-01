#include "stdafx.h"
#include "RingBuffer.h"

/**
Конструктор по-умолчанию
*/
CRingBuffer::CRingBuffer(void) {

    init();
}

/**
Инициализировать буфер
*/
void CRingBuffer::init(void) {

    this->size = RING_BUFFER_SIZE;
    readPos = writePos = count = 0;
}

/**
записать в буфер байт.
Возвращает количество байт фактически записанных в буфер, т.е. фактически 0 или 1
*/
uint8_t CRingBuffer::writeByte(uint8_t byte) {

    if (count == size) {
        // нет места в буфере
        return 0;
    }

    buffer[writePos++] = byte;
    count++;
    writePos = (writePos == size)? 0: writePos;

    return 1;
}

/**
Записать массив байт в буфер.
Будет возвращено реально записанное количество байт
*/
uint8_t CRingBuffer::write(const uint8_t* data, uint8_t count) {

    uint8_t cnt = 0;
    for (uint8_t i=0; i<count; i++) {
        if (writeByte(data[i]) == 0) {
            break;
        }
        cnt++;
    }

    return cnt;
}

/**
Прочитать из буфера байт в переменную c
Если нет данных в буфере, то возвращается 0, если есть, то возвращается 1
*/
int8_t CRingBuffer::read(uint8_t& c) {

    if (count == 0) {
        // нет данных в буфере
        return 0;
    }

    // считать байт
    c = buffer[readPos++];

    // пересчитать позицию
    count--;
    readPos = (readPos == size)? 0: readPos;

    return 1;
}

/**
Прочитать из буфера нужное количество байт.
Если в буфере есть такие данные, то возвратить число прочитанных байтов,
если такого количества данных в буфере нет, то возвратить 0.
Предполагается, что count не больше размера буфера buf.
*/
int8_t CRingBuffer::read(uint8_t* buf, int8_t bytesToRead) {

    if (count < bytesToRead) {
        // столько данных, сколько надо в буфере еще нет.
        return 0;
    }

    for (int i=0; i<bytesToRead; i++) {
        uint8_t c;
        read(c);
        buf[i] = c;
    }

    return bytesToRead;
}

/**
Возвращает количество байт, доступных для чтения
*/
uint8_t CRingBuffer::available(void) {

    return count;
}
