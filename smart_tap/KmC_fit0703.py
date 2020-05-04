import csv
import sys
import time
import datetime
import pytz
import RPi.GPIO as GPIO
from time import sleep
import Adafruit_GPIO.SPI as SPI
import Adafruit_MCP3008

from matplotlib import pyplot as plt
import operator
import numpy as np
import math
import pandas as pd
import random

SPI_PORT   = 0
SPI_DEVICE = 0
mcp = Adafruit_MCP3008.MCP3008(spi=SPI.SpiDev(SPI_PORT, SPI_DEVICE))

PIN = [0, 1, 2, 3]
RELAY = [26, 19, 13, 6]

#Init GPIO Pins
def InitGPIO():
        GPIO.setmode(GPIO.BCM)
        GPIO.setwarnings(False)

#Init Relay Module as True state
def InitRelay():
        for i in range(len(RELAY)):
                GPIO.setup(RELAY[i], GPIO.OUT)
                GPIO.output(RELAY[i], True)

InitGPIO()
InitRelay()


def GetRawData():
	global PIN
	CountingTime = 5
	for i in range(4):
	        filename = 'rawdata'+str(i)+'.txt'
	        f = open(filename, 'wb')

	        for cnt in range(CountingTime * 60):
	                start_time = time.time()
	                while True:
	                        value = mcp.read_adc(PIN[i])
	                        f.write(str(value))
	                        f.write('\n')
	                        if(time.time() - start_time) > 1.0/60.0:
	                                f.write('\n\n\n')
	                                break
	        f.close()

def KmcFit():
	global time_past
	global time_now
	time_past = time_now
	time_now = datetime.datetime.now(tz=pytz.timezone('Asia/Seoul')).strftime('%Y-%m-%d %H:%M:%S')
	df_list = []
	for count in range(4):
		raw_data_file_name = './rawdata'+str(count)+'.txt'
		save_csv_file_name = './rawdata'+str(count)+'.csv'

		f = open(raw_data_file_name, 'r')

		datalist = []
		while True:
			list1 = []
			while True:
				data = f.readline()
				if data=='':
					break
				if (data != '\n'):
					list1.append(int(data))
				elif data == '\n':
					f.readline()
					f.readline()
					break
			datalist.append(list1)
			if data == '':
				break
		f.close()

		datalist.pop()
		print('size of data :'+str(len(datalist)))
		print('-----samples per 1 cycle ----')
		#for cnt in range(len(datalist)):
		#    print(len(datalist[cnt]),end=' ')
		#print('')

		#for i in range(len(datalist)):
		#    print(len(datalist[i]))
		print('-----------------------------')

		data = []
		num = 0

		for num in range(len(datalist)-1):
			effectiveness = len(datalist[num])/len(datalist[num+1])
			if (0.8 > effectiveness or effectiveness > 1.2):
				continue

			x_axis = len(datalist[num])+len(datalist[num+1])
			y_axis = datalist[num] + datalist[num+1]

			index, value_min = min(enumerate(y_axis), key=operator.itemgetter(1))
			value_max = max(y_axis)

			for i in range(index):
				y_axis.append(y_axis[0])
				y_axis.pop(0)

			value_calibration = int(value_min)
			for i in range(x_axis):
				y_axis[i] -= value_calibration

			value_max = max(y_axis)
			value_min = min(y_axis)

			frequency = 2
			x1 = np.arange(0, x_axis, 1)

			y1_temp = []
			for i in range(len(x1)):
				y1_temp.append(float(x1[i])/float(x_axis))
			y1_temp = np.array(y1_temp)

			y1 = (-((value_max - value_min)/2)*np.cos(2*np.pi*frequency*(y1_temp)) + (value_max - value_min)/2)

			error = 0
			for cnt in range(x_axis):
				if(y1[cnt]!=0 and y_axis[cnt]!=0):
					temp= math.fabs(y1[cnt]/y_axis[cnt])
					if(temp <= 1):
						temp= math.fabs(y_axis[cnt]/y1[cnt])
					error += temp

			error1 = error/x_axis
			error2 = error1 * (value_max-value_min)/2

			rms_cos = ((value_max - value_min)/2)/math.sqrt(2)
			temp = 0
			for cnt in range(x_axis):
				temp += y_axis[cnt]**2
			rms_plot = math.sqrt(temp / x_axis)

			data_temp = [round(error1, 2), round(error2, 2), round(rms_cos, 2), round(rms_plot, 2)]
			data.append(data_temp)

		print(time_past, time_now)
		data.insert(0, [str(time_past), str(time_now), int(PIN[count]), 'NaN'])
		df = pd.DataFrame(data)
		df_list.append(df)

	writer = pd.ExcelWriter('rawdata_sheet.xlsx', engine='xlsxwriter')
	df_list[0].to_excel(writer, sheet_name = 'Sheet1')
	df_list[1].to_excel(writer, sheet_name = 'Sheet2')
	df_list[2].to_excel(writer, sheet_name = 'Sheet3')
	df_list[3].to_excel(writer, sheet_name = 'Sheet4')
	writer.save()



#GetRawData()

#(temporary) test for 'time_past'
time_past = datetime.datetime.now(tz=pytz.timezone('Asia/Seoul'))
time_past += datetime.timedelta(minutes = -2)
time_past = time_past.strftime('%Y-%m-%d %H:%M:%S')
time_now = datetime.datetime.now(tz=pytz.timezone('Asia/Seoul'))
time_now += datetime.timedelta(minutes = -1)
time_now = time_now.strftime('%Y-%m-%d %H:%M:%S')

KmcFit()
