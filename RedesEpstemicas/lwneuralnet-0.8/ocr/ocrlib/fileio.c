/* fileio.c -- file I/O for ocr application
 * copyright (c) 2003 Peter van Rossum
 * $Id: fileio.c,v 1.2 2003/10/19 04:25:13 petervr Exp $ */

#include <stdio.h>
#include <stdlib.h>
#include <zlib.h>
#include "ocr.h"

/****************************************
 * FILE *I/O
 ****************************************/

void
fscan_training_digit (FILE *file, training_data_t *data)
{
  int x, y;
  char digit[32][32];
  char line[100];
  for (y = 0; y < 32; y++) {
    /* skip lines that do not start with 0 or 1 */
    do {
      fgets (file, line, 80);
    } while ((line[0] != '0')
             && (line[0] != '1'));
    /* 32 0/1's per line */
    for (x = 0; x < 32; x++) {
      digit[x][y] = (line[x] == '0' ? 0 : 1);
    }
  }

  preprocess_char32x32 (digit, 0, data->intensity);
  fgets (file, line, 80);
  sscanf (line, " %c\n", &data->value);
}

int
fscan_training_file (FILE *file, training_data_t *array)
{
  int i;

  i = 0;
  while (!feof (file)) {
    fscan_training_digit (file, &array[i]);
    i++;
  }

  return i;
}

int
read_training_file (const char *filename, training_data_t *array)
{
  int result;

  FILE *file;
  file = fopen (filename, "r");
  result = fscan_training_file (file, array);
  fclose (file);

  return result;
}
