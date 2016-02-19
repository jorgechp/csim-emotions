#!/bin/bash

CONTADOR=1
for i in ./*; do	
	if [ $i != $0 ]; then
		nuevoNombre="af_3_"$CONTADOR".JPG"
	     	mv $i $nuevoNombre 	
		CONTADOR=$((CONTADOR + 1))
	fi

done
