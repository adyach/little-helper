#!/bin/bash
avrdude -p m8 -c avrisp -P /dev/ttyACM0 -b 19200 -U flash:w:/home/andrey/Programming/Projects/LittleHelper/LittleHelper/src_controller/LittleHelperController/Release/LittleHelperController.hex
