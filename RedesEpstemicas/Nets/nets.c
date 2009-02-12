

#include "nets.h" /*cabeçalhos, fontes e bibliotecas*/



/*
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 ******************************************************************							MAIN							*********
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 */

int main(int argc, char *argv[])
{
	char nome_arq[40];
	FILE * arq;

	inicia_semente_para_numeros_pseudo_aleatorios();

/*Arquivo de Log*/
	sprintf(nome_arq, "log.txt");
	if ((arq_log = fopen(nome_arq,"w")) == NULL) 
	{	printf("Nao foi possivel abrir arquivo %s\n", nome_arq);	exit(0);	}

/*Arquivo de Foco*/
	sprintf(nome_arq, "foco.txt");
	if ((arq_foco = fopen(nome_arq,"a")) == NULL) 
	{	printf("Nao foi possivel abrir arquivo %s\n", nome_arq);	exit(0);	}

/*Arquivo de Foco*/
	sprintf(nome_arq, "coordenadas.txt");
	if ((arq_coordenadas = fopen(nome_arq,"a")) == NULL) 
	{	printf("Nao foi possivel abrir arquivo %s\n", nome_arq);	exit(0);	}

/*Arquivo de Foco*/
	sprintf(nome_arq, "matriz_w.txt");
	if ((arq_matriz_w = fopen(nome_arq,"a")) == NULL) 
	{	printf("Nao foi possivel abrir arquivo %s\n", nome_arq);	exit(0);	}

/*Parâmetros*/

/*Arquivo de Entrada*/
	sprintf(nome_arq, "nets.txt");
	if ((arq = fopen(nome_arq,"r")) == NULL) 
	{	printf("Nao foi possivel abrir arquivo %s\n", nome_arq);	exit(0);	}

	fscanf(arq, "%d", &NUMERO_DE_EPISTRONS);					/*número de espistrons*/
	fscanf(arq, "%d", &TEMPO_DE_REFRESH_DA_FUNCAO_TIMER_EM_MSECS);		/*atualização da tela, em msecs*/
	fscanf(arq, "%d", &RODADAS_DO_ALGORITMO_DE_SIMULACA0_ANTES_DE_RENDERIZAR);		/*rodadas de atualizaçao antes de renderizar*/
	fscanf(arq, "%f", &PONDERADOR_DA_QUANTIDADE_DE_TREINOS_NA_REDE_NEURONAL_EM_CIMA_DO_PESO_W);			/*constante que multiplica o peso w*/
																	/* que irá dizer quanto a rede neuronal vai ser treinada*/
	fscanf(arq, "%f", &TAXA_DE_APRENDIZADO_DA_REDE_EPISTEMICA); /*Taxa inicial de aprendizado, usada na regra de Hebb*/
	fscanf(arq, "%f", &ATUALIZACAO_TX_APRENDIZADO); /**/
	fscanf(arq, "%d", &ATUALIZA_A_TX_APRENDIZADO_A_CADA); /**/


	fscanf(arq, "%d", &GERA_WS_ALEATORIAMENTE); /**/
	fscanf(arq, "%d", &ATUALIZAR_WS_EM_MODO_COMPETITIVO); /**/

	fscanf(arq, "%d", &NORMALIZA_MATRIZ_W); /**/
	fscanf(arq, "%d", &NORMALIZA_MATRIZ_W_A_CADA); /**/

	fscanf(arq, "%d", &GERA_COORDENADAS_ALEATORIAMENTE); /**/
	fscanf(arq, "%f", &CONSTANTE_QUE_PONDERA_A_DISTANCIA_QUE_O_EPISTRON_CAMINHA_EM_DIRECAO_A_RESULTANTE); /**/
	fscanf(arq, "%f", &ATUALIZACAO_DA_CONSTANTE_QUE_PONDERA_A_DISTANCIA_QUE_O_EPISTRON_CAMINHA_EM_DIRECAO_A_RESULTANTE); /**/
	fscanf(arq, "%d", &ATUALIZAR_A_CONSTANTE_QUE_PONDERA_A_DISTANCIA_QUE_O_EPISTRON_CAMINHA_EM_DIRECAO_A_RESULTANTE_A_CADA); /**/

	fscanf(arq, "%f", &MAX_RAIO_FOCO); /**/
	fscanf(arq, "%f", &LIMITE_SUPERIOR_COORDENADA_DO_FOCO); /**/	
	fscanf(arq, "%d", &GERA_FOCOS_ALEATORIAMENTE); /**/
	
	fscanf(arq, "%d", &GERAR_LOG_A_CADA); /**/
	fscanf(arq, "%d", &EXPORTAR_PARA_IMAGEM_A_CADA); /**/

 	fclose(arq);


/*Inicialização ou Entrada*/
	
	if (GERA_WS_ALEATORIAMENTE)
		cria_matriz_de_peso_w_aleatoriamente(); /* gera matriz de pesos */
	else
	{
		int i,j;

	/*Arquivo de Ws*/
		sprintf(nome_arq, "coordenadas_entrada.txt");
		if ((arq = fopen(nome_arq,"r")) == NULL) 
		{	printf("Nao foi possivel abrir arquivo %s\n", nome_arq);	exit(0);	}
				

		for (i=0; i < NUMERO_DE_EPISTRONS ;i++ )
			for (j=0; j < NUMERO_DE_EPISTRONS ;j++ )
					fscanf(arq, "%f", &W[i][j]);
		fclose(arq);
	}

	if (GERA_COORDENADAS_ALEATORIAMENTE)	
		gera_coordenadas_dos_epistrons_aleatoriamente(); /* gera coordenadas */
	else
	{

	/*Arquivo de Foco*/
		sprintf(nome_arq, "coordenadas_entrada.txt");
		if ((arq = fopen(nome_arq,"r")) == NULL) 
		{	printf("Nao foi possivel abrir arquivo %s\n", nome_arq);	exit(0);	}

		int  k;

		for (k=0 ;  k<NUMERO_DE_EPISTRONS ; k++ )
		{	
				fscanf(arq, "%f", &POSICAO_DO_EPISTRON[k].X );
				fscanf(arq, "%f", &POSICAO_DO_EPISTRON[k].Y);	
		}
		//printf("%.10f %.10f\n", POSICAO_DO_EPISTRON[k].X , POSICAO_DO_EPISTRON[k].Y);


	}


	if (GERA_FOCOS_ALEATORIAMENTE)	
		gera_focos_dos_agentes_aleatoriamente();
	else
	{
				int i,j;
			/*Arquivo de Foco*/
				sprintf(nome_arq, "foco_entrada.txt");
				if ((arq = fopen(nome_arq,"r")) == NULL) 
				{	printf("Nao foi possivel abrir arquivo %s\n", nome_arq);	exit(0);	}

			/*lê coordenadas_e raios de_todos_os_centroides_de_foco_aleatoriamente() */
				for (i=0;i<NUMERO_MAXIMO_DE_EPISTRONS ; i++)
				{
					for (j=0;j<NUMERO_DE_COORDENADAS_DO_FOCO ; j++)
							fscanf(arq, "%f", &CENTROIDE[i][j]);

					fscanf(arq, "%f", &RAIO_DO_FOCO[i]);
				}
				fclose(arq);
	}
	
	


/*Inicialização*/

	inicia_redes_neuronais(); /* cria redes neuronais */
	inicia_glut_opengl(&argc, argv);

	EPISTRON_SORTEADO = (int)numero_pseudo_aleatorio_positivo_menor_que(NUMERO_DE_EPISTRONS);

/*Inicia a Simulação*/
	glutMainLoop();	/*mostra todas as janelas e começa a máquina de estados*/

/*Finalização*/
	libera_recursos_das_redes_neuronais();
 	fclose(arq_log);
 	fclose(arq_foco);
 	fclose(arq_coordenadas);
 	fclose(arq_matriz_w);


	return 0;
}






/*
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 ******************************************************************					NÚMEROS PSEUDO-ALEATÓRIOS				*********
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 */

void inicia_semente_para_numeros_pseudo_aleatorios()
	{	srand(   (unsigned) ( (time(NULL) / 2) ) );  }

float numero_pseudo_aleatorio_positivo_menor_que(float max)
	{
	float x = rand()-1;
	float y = RAND_MAX;

	x/=y;	/*numero >=0, <1*/
	x*=max; /*numero >=0, <max*/

	return fabs(x); /* max > x >= 0 */
	}





