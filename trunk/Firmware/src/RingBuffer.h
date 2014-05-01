// RingBuffer.h

#pragma once

#ifndef RING_BUFFER_SIZE
#define RING_BUFFER_SIZE 42
#endif

/**
Кольцевой буфер
         |writePos
[XXXHelloXX]
    ^readPos

pos - позиция, которая будет прочитана из буфера, при следующем вызове read
write - позиция, которая будет записана при следующей записи write
*/
class CRingBuffer
{
private:
    /**
    Указатель на область памяти, содержащей буфер
    */
    uint8_t buffer[RING_BUFFER_SIZE];

    /**
    Размер буфера в байтах
    */
    uint8_t size;

    /**
    Текущая позиция не чтение
    */
    uint8_t readPos; 

    /**
    Текущая позиция на запись
    */
    uint8_t writePos;

    /**
    Количество данных, доступных для чтения
    */
    uint8_t count;

public:
    CRingBuffer(void);
    void init(void);

    uint8_t writeByte(uint8_t byte);
    uint8_t write(const uint8_t* data, uint8_t count);
    int8_t read(uint8_t& byte);
    int8_t read(uint8_t* buf, int8_t bytesToRead);
    uint8_t available(void);
};

