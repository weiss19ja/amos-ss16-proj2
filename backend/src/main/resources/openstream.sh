#!/bin/bash

raspivid -o - -t 0 -hf -w 640 -h 360 -fps 25 | cvlc -vvv stream:///dev/stdin --sout '#standard{access=http,mux=ts,dst=0.0.0.0:8001}' :demux=h264

