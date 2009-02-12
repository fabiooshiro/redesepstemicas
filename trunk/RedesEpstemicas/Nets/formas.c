
/*
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 ******************************************************************					OPENGL - FORMAS E CORES					*********
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 */

/****************************************
 * circulo
 ****************************************/
void CIRCULO(float X, float Y, float RAIO, int cheio)
{

	int i;//, j; 
	float angulo, DIAMETRO=RAIO*2;
	
		if (cheio)  
			glBegin(GL_POLYGON);   /*CIRCULO preenchido*/
		else		
			glBegin(GL_LINE_LOOP); /*CIRCULO vazio*/

		for (i = 0; i < PRECISAO_DO_CIRCULO; i++) 
		{	
				angulo = 2 * M_PI * i / PRECISAO_DO_CIRCULO;

				glVertex2d( DIAMETRO*cos(angulo)+X, 
						    DIAMETRO*sin(angulo)+Y );  
		}

		glEnd();
}

/****************************************
 * linha
 ****************************************/
void LINHA(float x1, float y1, float x2, float y2)
	{ glBegin(GL_LINES); glVertex2d(x1,y1); glVertex2d(x2,y2); glEnd(); }


/****************************************
 * texto
 ****************************************/
void TEXTO(float x, float y, char *str, void *fonte)
	{
	  int tam, i;
	  glRasterPos2d(x, y); /*início da posição - para imprimir bitmaps*/
	  tam = (int) strlen(str);
	  for (i = 0; i < tam; i++) 
		  glutBitmapCharacter(fonte, str[i]);
	}

/*  Fontes disponíveis: 

	GLUT_BITMAP_8_BY_13				
	GLUT_BITMAP_9_BY_15
	GLUT_BITMAP_TIMES_ROMAN_10		
	GLUT_BITMAP_TIMES_ROMAN_24
	GLUT_BITMAP_HELVETICA_10		
	GLUT_BITMAP_HELVETICA_12		
	GLUT_BITMAP_HELVETICA_18
//*/
