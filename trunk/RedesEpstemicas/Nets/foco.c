

/*
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 ******************************************************************		CONHECIMENTO DA REDE NEURONAL	 ::		FOCO		*********
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 */
void gera_focos_dos_agentes_aleatoriamente()
{
	int i,j;

/*gera_coordenadas_e raios de_todos_os_centroides_de_foco_aleatoriamente() */
	for (i=0;i<NUMERO_MAXIMO_DE_EPISTRONS ; i++)
	{
		for (j=0;j<NUMERO_DE_COORDENADAS_DO_FOCO ; j++)
			CENTROIDE[i][j] = numero_pseudo_aleatorio_positivo_menor_que(LIMITE_SUPERIOR_COORDENADA_DO_FOCO); 
		
		RAIO_DO_FOCO[i] = numero_pseudo_aleatorio_positivo_menor_que(MAX_RAIO_FOCO); 
	}

}




void gera_tupla_arg1_pertencente_ao_FOCO_da_rede_arg0(int k, float *tupla)
{
	int i;  
	//int j=0; 
	float soma;

	do	
	{	
			for (soma=0, i=0;i<NUMERO_DE_COORDENADAS_DO_FOCO ; i++)
			{
				tupla[i] = numero_pseudo_aleatorio_positivo_menor_que(LIMITE_SUPERIOR_COORDENADA_DO_FOCO);		
				
				soma +=  pow(tupla[i] - CENTROIDE[k][i] ,2)  ;	
			}
	}	
	while (	pow(RAIO_DO_FOCO[k],2)		>=		soma		); /*equaçao da esfera/circunferencia*/

}


