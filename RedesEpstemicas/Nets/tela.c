

/*
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 ******************************************************************				OPENGL	::	 GLUT							*********
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 */

/****************************************
 * inicia_opengl: inicia glut e opengl, cria a janela
 ****************************************/
void inicia_glut_opengl(int *argc, char *argv[])
{
	glutInit			   (argc, argv);							/*inicia glut (para janela) */
	glutInitDisplayMode	   (GLUT_DOUBLE | GLUT_RGB);				/*single ou float buffer single, tipo de cores rgb*/
	glutInitWindowSize	   (LARGURA_DA_TELA, ALTURA_DA_TELA);		/*largura, altura, em pixels*/
	glutInitWindowPosition (POSICAO_X_INICIAL_DA_JANELA,
							POSICAO_Y_INICIAL_DA_JANELA);			/*posi��o do extremo esquerdo superior*/
	ID_DA_JANELA_PRINCIPAL =
		glutCreateWindow   (NOME_DA_JANELA_PRINCIPAL);				/*cria janela(mas n�o mostra)*/

	glClearColor	(1.0, 1.0, 1.0, 1.0);						/*cor que vai limpar a janela RGBA*/
	glViewport		(0, 0, 	LARGURA_DA_TELA, ALTURA_DA_TELA);	/*visao pra tela toda, extremos do ortho nos extremos da tela*/

	gluOrtho2D		(0.0, 1.0, 0.0, 1.0);     /* define limites de coordenadas x, y (ORTHO): infx, supx, infy, supy*/

	glMatrixMode    (GL_PROJECTION); /*usa as coordenadas em ortho, espalha ortho na janela toda*/

	glLoadIdentity();				 /*carrega a matriz padr�o*/

	glutDisplayFunc	   (display); 		/*	desenha usando a fun��o display (necess�ria) */
	glutReshapeFunc	   (reshape);		/*	fun��o que � executada em caso de resize*/
	//glutIdleFunc       (idle); 		/*fun��o que roda quando o sistema est� ocioso*/
	glutTimerFunc      (TEMPO_DE_REFRESH_DA_FUNCAO_TIMER_EM_MSECS, atualiza_rede_epistemica,1); 	/*fun��o chamada de tempo em tempo(1� arg, em msecs)*/
	glutKeyboardFunc   (keyboard);								/*fun��o para evento tecla*/
	glutMouseFunc      (mouse);									/*fun��o para evento mouse*/

}


/****************************************
 * reshape: chamada quando a tela � redimensionada
 ****************************************/
void reshape (int w, int h)
	{
	LARGURA_DA_TELA = w;
	ALTURA_DA_TELA  = h;

	printf("dimens�o da janela alterada: %d x %d\n",w, h);


	/*atualiza visao pra tela toda, extremos do ortho nos extremos da tela*/
	glViewport (0, 0, LARGURA_DA_TELA, ALTURA_DA_TELA);

	gluOrtho2D		(0.0, 1.0, 0.0, 1.0);     /* define limites de coordenadas x, y (ORTHO): infx, supx, infy, supy*/

	glMatrixMode (GL_PROJECTION);
	glLoadIdentity(); /*carrega a matriz */

	/*e chama o display de novo*/
	}


/****************************************
 * keyboard: chamada quando uma tecla � acionada
 ****************************************/
void keyboard(unsigned char key, int x, int y)
{
	switch (key)
	{
		case 27:  glFinish(); exit(0);  /*Esc*/
	}

}

//*********	if (!glutGetWindow()) {glFinish(); exit(0); } /*aonde vou por isso?*/



/****************************************
 * mouse: chamada quando o mouse � acionado
 ****************************************/
void mouse(int botao, int estado, int x, int y)
{
	switch(botao)
	{
		case GLUT_LEFT_BUTTON:
			if (estado == GLUT_DOWN) {
				PAUSADO = !PAUSADO;
				printf("pausado = %d",PAUSADO );
			}
		case GLUT_MIDDLE_BUTTON:  if (estado == GLUT_DOWN) {  }
		case GLUT_RIGHT_BUTTON:   if (estado == GLUT_DOWN) {  }
	}
	//printf("mouse x %d, y %d",x,y);

}



/****************************************
 * * Exporta o buffer atual do opengl para um arquivo bmp
 ****************************************/
void EXPORTA_TELA_ATUAL_PARA_IMAGEM()
{
	char nome[20];

	sprintf(nome, "%d.bmp", contator_RODADAS_DO_ALGORITMO_DE_ATUALIZACAO_DA_REDE);

	dumpBMP(nome, LARGURA_DA_TELA, ALTURA_DA_TELA);

}

int dumpBMP(char *szName, int width, int height)
{
    int nByteWidth = (3 * width + 3) & ~3;
    unsigned char pixels [nByteWidth * height];
	register int x, y;
	FILE *fp;
	BITMAPFILEHEADER hdr;
    BITMAPINFOHEADER bih;

    fp = fopen(szName, "wb");

    for (y = 0; y < height; y++)
	{
        unsigned char mid, *p = pixels + y * nByteWidth;

        glReadPixels(0, y, width, 1, GL_RGB, GL_UNSIGNED_BYTE, (GLvoid *) p);
        for (x = 0; x < width; x++)
		{
            mid = p[0];
            p[0] = p[2];
            p[2] = mid;
            p += 3;
        }
    }

    // Bitmap info header
    bih.biSize = sizeof(BITMAPINFOHEADER);
    bih.biWidth = width;
    bih.biHeight = height;
    bih.biPlanes = 1;
    bih.biBitCount = 24;
    bih.biCompression = BI_RGB;
    bih.biSizeImage = nByteWidth * height;
    bih.biXPelsPerMeter = 0;
    bih.biYPelsPerMeter = 0;
    bih.biClrUsed = 0;
    bih.biClrImportant = 0;

    // Bitmap file header
    hdr.bfType = 0x4d42;
    hdr.bfOffBits = sizeof(BITMAPFILEHEADER) + bih.biSize;
    hdr.bfSize = hdr.bfOffBits + nByteWidth * height;
    hdr.bfReserved1 = 0;
    hdr.bfReserved2 = 0;

    // Write to file
    fwrite(&hdr, 1, sizeof(hdr), fp);
    fwrite(&bih, 1, sizeof(bih), fp);
    fwrite(pixels, 1, nByteWidth * height, fp);

    fclose(fp);
    //free(pixels);

    return 0;
}








