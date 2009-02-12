

/*
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 ******************************************************************								RENDERIZAÇÃO				*********
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 */

/****************************************
 * GERA as coordenadas X e Y dos EPISTRONS, aleatoriamente
 ****************************************/
void gera_coordenadas_dos_epistrons_aleatoriamente()
{
	int  k;

	for (k=0 ;  k<NUMERO_DE_EPISTRONS ; k++ )
	{
			POSICAO_DO_EPISTRON[k].X = numero_pseudo_aleatorio_positivo_menor_que(1);
			POSICAO_DO_EPISTRON[k].Y = numero_pseudo_aleatorio_positivo_menor_que(1);
			//printf("%.10f %.10f\n", POSICAO_DO_EPISTRON[k].X , POSICAO_DO_EPISTRON[k].Y);
	}


/*
////////////////////////
	POSICAO_DO_EPISTRON[0].X = 0.1;
	POSICAO_DO_EPISTRON[0].Y = 0.1;
	POSICAO_DO_EPISTRON[1].X = 0.9;
	POSICAO_DO_EPISTRON[1].Y = 0.9;
////////////////////////
*/

}

/****************************************
 * GERA as coordenadas X e Y dos EPISTRONS, em forma de GRADE
 ****************************************
void gera_coordenadas_dos_epistrons_em_grade()
{
	int  k;
	float acum_i, acum_j, raiz, intervalox, intervaloy;

	raiz = sqrt(NUMERO_DE_EPISTRONS)+1;
	intervalox = LIMITE_SUPERIOR_X/raiz ;
	intervaloy = LIMITE_SUPERIOR_Y/raiz ;

	for (k=0, acum_i=intervalox; acum_i<LIMITE_SUPERIOR_X && k<NUMERO_DE_EPISTRONS ; acum_i+=intervalox )
		for (acum_j=intervaloy; acum_j<LIMITE_SUPERIOR_Y  && k<NUMERO_DE_EPISTRONS ; acum_j+=intervaloy, k++ )
		{
			POSICAO_DO_EPISTRON[k].X = acum_i; POSICAO_DO_EPISTRON[k].Y = acum_j;
		}
}
//*/

/****************************************
 * Calcula RESULTANTE do EPISTRON
 ****************************************/
/*
 * A resultante do Epistron é calculada geometricamente, ponderada pela importância que cada um dá a ele
 */
tipoCOORD  CALCULA_RESULTANTE_DO_EPISTRON(int a)
{
	printf("CalculaResultanteEpistron(%d)\n",a);
	int i,conexoes=0;
	tipoCOORD R;

	R.X = 0;
	R.Y = 0;
	float zero = 0;
	for (i=0;i<NUMERO_DE_EPISTRONS ;i++ )
	{
		if (	i != a	/*nao é ele mesmo*/	&& W[a][i] > zero	/*tem conexão*/)
		{
			R.X += (POSICAO_DO_EPISTRON[i].X - POSICAO_DO_EPISTRON[a].X)* W[a][i];
			R.Y += (POSICAO_DO_EPISTRON[i].Y - POSICAO_DO_EPISTRON[a].Y)* W[a][i];
			conexoes++;
		}
	}
	/*ao final, R : resultante, começando na origem. */

	printf("\tconexoes %d\n" , conexoes);
	printf("\tR[%d].X=%lf\n\tR[%d].Y=%lf\n",a,R.X,a,R.Y);

	return R;
}




/****************************************
 * Calcula a NOVA POSIÇÃO do EPISTRON
 ****************************************/
/*
 * Baseado na resultante, calcula a nova posição de cada Epistron
 */
void CALCULA_NOVAS_POSICOES_DOS_EPISTRONS()
{
	int i;
	tipoCOORD R;

	//ATUALIZA CTE

 /*Calcula resultantes do Epistrons*/
	for (i=0;i<NUMERO_DE_EPISTRONS ;i++ )
		RESULTANTE_DO_EPISTRON[i] = CALCULA_RESULTANTE_DO_EPISTRON(i);


	for (i=0;i<NUMERO_DE_EPISTRONS ;i++ )
	{

	/*Calcula a resultante antes de andar (nesse caso, não é necessário calcular antes)*/
		//RESULTANTE_DO_EPISTRON[i] = CALCULA_RESULTANTE_DO_EPISTRON(i);  /*(Re)Calcula a resultante antes de andar*/

		R = RESULTANTE_DO_EPISTRON[i];

	/*Estabiliza o modelo deixando as resultantes ponderadas só pelos pesos*/
		//R.X /= (NUMERO_DE_EPISTRONS-1);
		//R.Y /= (NUMERO_DE_EPISTRONS-1);

	/*Normaliza a resultante*
		R.X /= sqrt(	pow(R.X, 2) + pow(R.Y, 2) )	;
		R.Y /= sqrt(	pow(R.X, 2) + pow(R.Y, 2) )	;
	//*/
	/*Diminui a resultante para quanto o Epistron vai andar*/
		//R.X *= CONSTANTE_QUE_PONDERA_A_DISTANCIA_QUE_O_EPISTRON_CAMINHA_EM_DIRECAO_A_RESULTANTE;
		//R.Y *= CONSTANTE_QUE_PONDERA_A_DISTANCIA_QUE_O_EPISTRON_CAMINHA_EM_DIRECAO_A_RESULTANTE;

	/*Anda*/
		//POSICAO_DO_EPISTRON[i].X += R.X;
		//POSICAO_DO_EPISTRON[i].Y += R.Y;

	}

}

/****************************************
 * Representa a RESULTANTE do EPISTRON no Gráfico
 ****************************************/
/*
 * Calcula a representação da resultante no gráfico
 */
void CALCULA_POSICOES_DAS_RESULTANTES_NO_GRAFICO()
{
	int i;
	float maior = 0, tamanho;

	//ATUALIZA CTE

	for (i=0;i<NUMERO_DE_EPISTRONS ;i++ )
	{
	/*Re-calcula resultantes*/
		//RESULTANTE_DO_EPISTRON[i] = CALCULA_RESULTANTE_DO_EPISTRON(i);

	/*Tamanho da resultante (distância para origem) para saber qual maior (e menor)*/
		tamanho  = sqrt(pow(RESULTANTE_DO_EPISTRON[i].X, 2) + pow(RESULTANTE_DO_EPISTRON[i].Y, 2));
		if (tamanho<1 || tamanho>100)tamanho=1;

		if (tamanho > maior) maior = tamanho;
		//menor

			//printf("%f\n", tamanho);

	}


	for (i=0;i<NUMERO_DE_EPISTRONS ;i++ )
	{

		POSICAO_DA_RESULTANTE_NO_GRAFICO[i] = RESULTANTE_DO_EPISTRON[i];

	/*Tamanho da resultante (distância para origem) para setorizar*/
		tamanho  = sqrt(pow(RESULTANTE_DO_EPISTRON[i].X, 2) + pow(RESULTANTE_DO_EPISTRON[i].Y, 2));
		//if (tamanho<1)		tamanho = 1;
		//printf("%f\n", tamanho);

	/*Deixa o tamanho da resultante igual a 1*/
		if (tamanho >=1) POSICAO_DA_RESULTANTE_NO_GRAFICO[i].X /= tamanho;
		if (tamanho >=1) POSICAO_DA_RESULTANTE_NO_GRAFICO[i].Y /= tamanho;

		//printf("%f\n", POSICAO_DA_RESULTANTE_NO_GRAFICO[i].X);
		//printf("%f\n", POSICAO_DA_RESULTANTE_NO_GRAFICO[i].Y);

	/*Setoriza, diminuindo a resultante de acordo com seu tamanho (calculado antes) */
		if		(tamanho	<=	(maior*0.20))	{	POSICAO_DA_RESULTANTE_NO_GRAFICO[i].X *= 0.07;
													POSICAO_DA_RESULTANTE_NO_GRAFICO[i].Y *= 0.07;	}
		else if (tamanho	<=	(maior*0.45))	{	POSICAO_DA_RESULTANTE_NO_GRAFICO[i].X *= 0.08;
													POSICAO_DA_RESULTANTE_NO_GRAFICO[i].Y *= 0.08;	}
		else if (tamanho	<=	(maior*0.60))	{	POSICAO_DA_RESULTANTE_NO_GRAFICO[i].X *= 0.09;
													POSICAO_DA_RESULTANTE_NO_GRAFICO[i].Y *= 0.09;	}
		else if (tamanho	<=	(maior*0.75))	{	POSICAO_DA_RESULTANTE_NO_GRAFICO[i].X *= 0.10;
													POSICAO_DA_RESULTANTE_NO_GRAFICO[i].Y *= 0.10;	}
		else if (tamanho	<=	(maior*0.90))	{	POSICAO_DA_RESULTANTE_NO_GRAFICO[i].X *= 0.11;
													POSICAO_DA_RESULTANTE_NO_GRAFICO[i].Y *= 0.11;	}
		else if (tamanho	<=	(maior)	   )	{	POSICAO_DA_RESULTANTE_NO_GRAFICO[i].X *= 0.12;
													POSICAO_DA_RESULTANTE_NO_GRAFICO[i].Y *= 0.12;	}
		//*/
	//POSICAO_DA_RESULTANTE_NO_GRAFICO[i].X *= 0.12;
	//POSICAO_DA_RESULTANTE_NO_GRAFICO[i].Y *= 0.12;

	/*Com a resultante diminuída, soma à posição do Epistron*/
		POSICAO_DA_RESULTANTE_NO_GRAFICO[i].X += POSICAO_DO_EPISTRON[i].X ;//* OUTRACTE;
		POSICAO_DA_RESULTANTE_NO_GRAFICO[i].Y += POSICAO_DO_EPISTRON[i].Y ;//* OUTRACTE;


	}

}


/****************************************
 * Calcula RAIOs dos EPISTRONs
 ****************************************/
 /*
  * O raio do Epistron é proporcional à importância que outros Epistrons dão a ele
  */
void CALCULA_RAIOS_DOS_EPISTRONS()
{
	int i, j;
	float maior = 0;

/* O raio do Epistron é proporcional à importância que outros Epistrons dão a ele */
	for (i=0;i<NUMERO_DE_EPISTRONS ;i++ )
	{
		TAMANHO_DO_RAIO_NO_GRAFICO[i] = 0;

		for (j=0;j<NUMERO_DE_EPISTRONS ;j++ )
			if (	j != i			//Não é ele mesmo
					&& W[j][i] >= 0	//a conexão existe
				)

				TAMANHO_DO_RAIO_NO_GRAFICO[i] += W[j][i];

		if (TAMANHO_DO_RAIO_NO_GRAFICO[i]	>	maior)
			maior = TAMANHO_DO_RAIO_NO_GRAFICO[i];
			//menor
	}

	for (i=0;i<NUMERO_DE_EPISTRONS ;i++ )
	{


		//TAMANHO_DO_RAIO_NO_GRAFICO[i] =

		if		(TAMANHO_DO_RAIO_NO_GRAFICO[i]	<=	(maior*0.20))	TAMANHO_DO_RAIO_NO_GRAFICO[i] = 0.005;
		else if (TAMANHO_DO_RAIO_NO_GRAFICO[i]	<=	(maior*0.45))	TAMANHO_DO_RAIO_NO_GRAFICO[i] = 0.01;
		else if (TAMANHO_DO_RAIO_NO_GRAFICO[i]	<=	(maior*0.60))	TAMANHO_DO_RAIO_NO_GRAFICO[i] = 0.015;
		else if (TAMANHO_DO_RAIO_NO_GRAFICO[i]	<=	(maior*0.75))	TAMANHO_DO_RAIO_NO_GRAFICO[i] = 0.02;
		else if (TAMANHO_DO_RAIO_NO_GRAFICO[i]	<=	(maior*0.90))	TAMANHO_DO_RAIO_NO_GRAFICO[i] = 0.025;
		else if (TAMANHO_DO_RAIO_NO_GRAFICO[i]	<=	(maior)	    )	TAMANHO_DO_RAIO_NO_GRAFICO[i] = 0.03;


	}







}



/****************************************
 * DISPLAY
 ****************************************/
void display(void)
{
	int k;
	char str[20];

	glClear (GL_COLOR_BUFFER_BIT); /*limpa a janela*/

/*Calcula raios dos Epistrons = ws que chegam nele*/
	CALCULA_RAIOS_DOS_EPISTRONS();

/*Calcula novas posições dos Epistrons de acordo com suas resultantes*/
	CALCULA_NOVAS_POSICOES_DOS_EPISTRONS();

/*Calcula aonde vai ficar a resultante no gráfico*/
	CALCULA_POSICOES_DAS_RESULTANTES_NO_GRAFICO();


/*Desenho dos Epistrons*/
	glColor3fv (COR_PRETO);
	glLineWidth(1.2);


	for (k=0 ; k<NUMERO_DE_EPISTRONS ; k++ )
	{
		CIRCULO_VAZIO(	POSICAO_DO_EPISTRON[k].X , POSICAO_DO_EPISTRON[k].Y, 	TAMANHO_DO_RAIO_NO_GRAFICO[k]	); /*Epistron na nova posição, com novo Raio*/
		LINHA(	POSICAO_DO_EPISTRON[k].X,POSICAO_DO_EPISTRON[k].Y, POSICAO_DA_RESULTANTE_NO_GRAFICO[k].X, POSICAO_DA_RESULTANTE_NO_GRAFICO[k].Y); /*Representação de sua resultante*/
	}

/*Texto Contador*/
	glColor3fv (COR_VERMELHO);
	sprintf(str, "%d", contator_RODADAS_DO_ALGORITMO_DE_ATUALIZACAO_DA_REDE);
	TEXTO(	0.01, 0.98, str, GLUT_BITMAP_8_BY_13);


/*Exporta para imagem*/
	if (contator_RODADAS_DO_ALGORITMO_DE_ATUALIZACAO_DA_REDE % EXPORTAR_PARA_IMAGEM_A_CADA ==0)  EXPORTA_TELA_ATUAL_PARA_IMAGEM();

/*Círculo Alvo = EPISTRON_SORTEADO*/
	glLineWidth(1.5);
	glColor3fv (COR_PRETO);
	CIRCULO_VAZIO(	POSICAO_DO_EPISTRON[EPISTRON_SORTEADO].X , 	POSICAO_DO_EPISTRON[EPISTRON_SORTEADO].Y, 	TAMANHO_DO_RAIO_NO_GRAFICO[EPISTRON_SORTEADO]); //contorno
	glColor3fv (COR_CINZA);
	CIRCULO_CHEIO(	POSICAO_DO_EPISTRON[EPISTRON_SORTEADO].X , 	POSICAO_DO_EPISTRON[EPISTRON_SORTEADO].Y, 	TAMANHO_DO_RAIO_NO_GRAFICO[EPISTRON_SORTEADO]); //preenchimento

/*Alvo*/
	glLineWidth(1.0);
	glColor3fv (COR_PRETO);

	//LINHA(	POSICAO_DO_EPISTRON[EPISTRON_SORTEADO].X, 0.0,		POSICAO_DO_EPISTRON[EPISTRON_SORTEADO].X, 1.0);
	//LINHA(	0.0, 	POSICAO_DO_EPISTRON[EPISTRON_SORTEADO].Y,		1.0, 	POSICAO_DO_EPISTRON[EPISTRON_SORTEADO].Y);

/*Texto 'Thinking'*/
	glColor3fv (COR_VERMELHO);

	TEXTO(	POSICAO_DO_EPISTRON[EPISTRON_SORTEADO].X	+	TAMANHO_DO_RAIO_NO_GRAFICO[EPISTRON_SORTEADO]	+	0.01,
			POSICAO_DO_EPISTRON[EPISTRON_SORTEADO].Y	+	TAMANHO_DO_RAIO_NO_GRAFICO[EPISTRON_SORTEADO]	+	0.01, "Thinking", GLUT_BITMAP_8_BY_13);

/*Envia para o Buffer*/

	//glFlush();/*envia informações, desnecessário com SwapBuffers*/
	glutSwapBuffers (); /*Desenha e troca de buffer*/

}


	/*
	 *	Maior R.X ou R.Y possível:
	 *	o plano vai de 0 a 1, portanto o máximo é 1.
	 *	W[][] vai de 0 a 1, portanto o máximo é 1.
	 *	o ponderador será só o NÚMERO DE EPISTRONS, para o pior caso:
			tem conexão com TODOS,
			TODOS estão no extremo da tela.
	 *	Dividindo pelo NÚMERO DE EPISTRONS-1, a resultante fica dentro da tela.
	 *
	 */


