# Makefile -- makefile for ocr application
# $Id: Makefile,v 1.2 2003/10/19 04:25:13 petervr Exp $

CC := gcc
CFLAGS += -O2 -g -Wall -W -I../../source

libocr.a : train.o sample.o fileio.o
	ar cru $@ $^
	ranlib $@
 
indent :
	indent -TFILE -TgzFile -Ttraining_data_t -Tnetwork_t --no-tabs *.[ch]

clean : 
	-rm -f *.o libocr.a

clean-backup :
	-rm -f *.bak *~

clean-all : clean clean-backup

source-dist :
	-rm -f *.o libocr.a
	-rm -f *.bak *~
	-rm -f .cvsignore
