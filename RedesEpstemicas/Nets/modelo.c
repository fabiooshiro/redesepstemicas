

/*
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 ******************************************************************					SIMULAÇÃO - REDE EPISTÊMICA				*********
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 */
/*	W que sai: importância que o agente dá ao outro W[agente][0-N]
 *	W que entra: importância que o outro dá ao agente W[0-N][agente]	 */


/****************************************
 * Algoritmo de atualização da rede,  chamado a cada msecs definidos em TEMPO_DE_REFRESH_DA_FUNCAO_TIMER_EM_MSECS
 ****************************************/
void atualiza_rede_epistemica(int passo)
{
	if(PAUSADO)
	{
		glutTimerFunc(TEMPO_DE_REFRESH_DA_FUNCAO_TIMER_EM_MSECS, atualiza_rede_epistemica, 1);
		return;
	}
	int i, j, k;//int cont;
	float erro, delta;//float x;
	/*ao entrar nesta função, um EPISTRON_SORTEADO JÁ foi sorteado*/
	/*ao sair, um novo também é sorteado, para que seja trabalhado na próxima chamada*/

	for (i=0;i < RODADAS_DO_ALGORITMO_DE_SIMULACA0_ANTES_DE_RENDERIZAR ;i++, contator_RODADAS_DO_ALGORITMO_DE_ATUALIZACAO_DA_REDE++ )
	{
		for (j=0;j<NUMERO_DE_EPISTRONS ;j++ )
			if (	j != EPISTRON_SORTEADO
					//&& W[j][EPISTRON_SORTEADO] >= 0
				)
			{

				/*treina agente j com conhecimento do agente atual */
				/* de acordo com a importância que agente j dá para atual (w [j][atual])*/

					erro = (float)treina_rede_arg0_com_par_da_rede_arg1_arg2_vezes
						(j, EPISTRON_SORTEADO,
													PONDERADOR_DA_QUANTIDADE_DE_TREINOS_NA_REDE_NEURONAL_EM_CIMA_DO_PESO_W
													* W[j][EPISTRON_SORTEADO]); /**/


					delta = TAXA_DE_APRENDIZADO_DA_REDE_EPISTEMICA * (1.0/(erro+1.0)) * W[j][EPISTRON_SORTEADO]; /*regra de Hebb*/

					W[j][EPISTRON_SORTEADO] += delta; //atualiza w [j][sorteado]

						/*Desconta delta nas outras conexões*/
							if (ATUALIZAR_WS_EM_MODO_COMPETITIVO)
							{
								printf("Atualizando em modo competitivo\n");
								for (k=0;k<NUMERO_DE_EPISTRONS ;k++ )
									if (W[j][k]>=0 //tem conexão
										&& k != EPISTRON_SORTEADO //não é o atual
										&& k != j )//não é o mesmo
									{
											W[j][k] -= (delta	/	(NUMERO_DE_WS_SAINDO_DO_EPISTRON[j]-1)	);

											if (W[j][k] < 0) //PROTEÇÃO PARA W[j][sort] não exceder limite de conhecimento do Epistron
											{
												W[j][EPISTRON_SORTEADO] += W[j][k] ;
												W[j][k] = 0;
											}
									}

							}
								/**/



					if (contator_RODADAS_DO_ALGORITMO_DE_ATUALIZACAO_DA_REDE % GERAR_LOG_A_CADA == 0)	 /*mostra todas as alterações*/
					{
						fprintf(arq_log, "t: %8d",							contator_RODADAS_DO_ALGORITMO_DE_ATUALIZACAO_DA_REDE);
						fprintf(arq_log, ", treinos(rede): %3.0f",				PONDERADOR_DA_QUANTIDADE_DE_TREINOS_NA_REDE_NEURONAL_EM_CIMA_DO_PESO_W
																					* W[j][EPISTRON_SORTEADO]);
						fprintf(arq_log, ", erro: %13.10f",					erro );
						fprintf(arq_log, ", txap: %13.10f",					TAXA_DE_APRENDIZADO_DA_REDE_EPISTEMICA );
						fprintf(arq_log, ", delta: %13.10f",				delta );
						fprintf(arq_log, ", novoW[%2d][%2d]: %13.10f\n",	j,  EPISTRON_SORTEADO, W[j][EPISTRON_SORTEADO]);
					}

			}/*passou por todos os epistrons*/


	/*sorteia epistron para ser testado na próxima simulação*/
		EPISTRON_SORTEADO = (int) numero_pseudo_aleatorio_positivo_menor_que(NUMERO_DE_EPISTRONS);

	/*atualização da taxa de aprendizado*/
		if (contator_RODADAS_DO_ALGORITMO_DE_ATUALIZACAO_DA_REDE % ATUALIZA_A_TX_APRENDIZADO_A_CADA == 0)
			TAXA_DE_APRENDIZADO_DA_REDE_EPISTEMICA *= ATUALIZACAO_TX_APRENDIZADO;

	/*normalização da matriz w*/
	 if ( NORMALIZA_MATRIZ_W
		  && contator_RODADAS_DO_ALGORITMO_DE_ATUALIZACAO_DA_REDE % NORMALIZA_MATRIZ_W_A_CADA == 0)
		 normaliza_matriz_de_pesos();

	}/*próximo sorteado*/


	glutPostRedisplay(); /*chama o display de novo*/
	glutTimerFunc(TEMPO_DE_REFRESH_DA_FUNCAO_TIMER_EM_MSECS, atualiza_rede_epistemica, 1);

}




/****************************************
 * cria_matriz_de_peso_w_aleatoriamente
 ****************************************/
 /*
  * W[epistron][epistron]: Número de conexões saindo do Epistron (Número de W[epistron][])
  */
void cria_matriz_de_peso_w_aleatoriamente()
{
	int i, j;
	//float x;

	for (i=0; i < NUMERO_DE_EPISTRONS ;i++ )
		for (j=0; j < NUMERO_DE_EPISTRONS ;j++ )
			W[i][j] = numero_pseudo_aleatorio_positivo_menor_que(1);
			//W[i][j] = 1.0;

/*
	for (i=0; i < NUMERO_DE_EPISTRONS ;i++ )
	{
				NUMERO_DE_WS_SAINDO_DO_EPISTRON[i] = 0;
				for (j=0; j < NUMERO_DE_EPISTRONS ;j++ )
				{
					x = numero_pseudo_aleatorio_positivo_menor_que(1); //sorteia se vai ter ou nao conexão
					if (x > 0.5)
					{
						W[i][j] = numero_pseudo_aleatorio_positivo_menor_que(1);

						NUMERO_DE_WS_SAINDO_DO_EPISTRON[i]++;
					}
					else
						W[i][j] = -1;
				}
	}
//*/
/*
///////////////////////deixando dois Epistrons grandes nas pontas
	for (j=2; j < 5 ;j++ )
	{
		if (W[j][0]<0) NUMERO_DE_WS_SAINDO_DO_EPISTRON[j]++;
		W[j][0] = 0.7 + numero_pseudo_aleatorio_positivo_menor_que(0.3);
	}
	for (j=5 ; j < 8 ;j++ )
	{
		if (W[j][1]<0) NUMERO_DE_WS_SAINDO_DO_EPISTRON[j]++;
		W[j][1] = 0.7 + numero_pseudo_aleatorio_positivo_menor_que(0.3);
	}

///////////////////////
//*/



}

/****************************************
 * cria_matriz_de_peso_w_aleatoriamente_com variação crescente no peso
 * Faz com que alguns agentes tenham mais importância que outros
 ****************************************
void cria_matriz_de_peso_w_aleatoriamente_de_forma_crescente()
{
	int i, j; float x, sorteio, passo;

	sorteio = 0;
	passo = 1.0/(float)NUMERO_DE_EPISTRONS;

	for (i=0; i < NUMERO_DE_EPISTRONS ;i++,sorteio+=passo )
		for (j=0; j < NUMERO_DE_EPISTRONS ;j++ )
				W[j][i] = numero_pseudo_aleatorio_positivo_menor_que(sorteio);
}
//*/
/****************************************
 * Normaliza matriz de Pesos
 ****************************************/
void normaliza_matriz_de_pesos()
{
	int i, j;
	float norma = W[0][1];

	for (i=0; i < NUMERO_DE_EPISTRONS ;i++ )
		for (j=0; j < NUMERO_DE_EPISTRONS ;j++ )
				if (W[i][j] > norma && i!=j)
					norma = W[i][j];

	for (i=0; i < NUMERO_DE_EPISTRONS ;i++ )
		for (j=0; j < NUMERO_DE_EPISTRONS ;j++ )
			W[i][j] /= norma;
}

/**/









