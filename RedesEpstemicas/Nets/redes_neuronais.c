

/*
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 ******************************************************************					REDES NEURONAIS							*********
 ************************************************************************************************************************************
 ************************************************************************************************************************************
 */

/****************************************
 * Inicia, Cria todas as redes
 ****************************************/
void inicia_redes_neuronais()
{
	int k;
	for (k=0;k< NUMERO_MAXIMO_DE_EPISTRONS  ;k++ )
		REDE_NEURONAL[k] = cria_rede_neuronal_aleatoria();

}

/****************************************
 * Cria Rede Aleatória
 ****************************************/
TIPO_REDE_NEURONAL cria_rede_neuronal_aleatoria(	/*output_filename*/	)
{
	TIPO_REDE_NEURONAL rede;

	rede = net_allocate_l(NUMERO_DE_CAMADAS_DA_REDE_NEURONAL, NUMERO_DE_NEURONIOS_NA_CAMADA);

	net_use_bias (rede, 0); /*NÃO USAR BIAS*/

	/*net_print (rede);//*/
    /*net_save (output_filename, net);//*/

	return rede;
}

/****************************************
 * treina rede x com par da rede y w vezes
 ****************************************/
float treina_rede_arg0_com_par_da_rede_arg1_arg2_vezes(int x, int y, double w)
{
	float erro;//float total_error, error;
	int t; int i;  int n; int quant;
	float output_x [NUMERO_DE_NEURONIOS_NA_CAMADA_DE_SAIDA	];
	float output_y [NUMERO_DE_NEURONIOS_NA_CAMADA_DE_SAIDA	];
	float tupla_y  [NUMERO_DE_NEURONIOS_NA_CAMADA_DE_ENTRADA];

	gera_tupla_arg1_pertencente_ao_FOCO_da_rede_arg0(y, tupla_y);

	net_compute (REDE_NEURONAL[y], tupla_y, output_y); /*obtém a saída de y*/

       /* -e */
	t = 0;
	if (w<1) quant=0;
	else quant = (int)w;

	while ((t < quant) /*&& (total_error >= max_error)*/)
	{
		net_compute (REDE_NEURONAL[x], tupla_y, output_x);

		net_compute_output_error (REDE_NEURONAL[x], output_y);

		net_train (REDE_NEURONAL[x]);

		t++;

	}
	net_compute (REDE_NEURONAL[x], tupla_y, output_x);

	/*net_print (REDE_NEURONAL[x]);//*/

	n	=	NUMERO_DE_NEURONIOS_NA_CAMADA_DE_SAIDA;
	for (erro=0,i=0;i<n; i++)
		erro += fabs(output_y[i] - output_x[i]);

/************************/
/************************/
/************************/
/************************/
/************************/
/************************ normalizar?????????????????????????????????????????????????????????*/

	//printf("erro %.10f ", erro);

	/*proteções*
	if (erro >= 1000000000)
		erro = 0;
	if (erro >= 1)
		erro = 1;
	//*/

	return erro;

}

/****************************************
 * Libera Recursos
 ****************************************/
void libera_recursos_das_redes_neuronais()
{
	int k;
	for (k=0;k< NUMERO_MAXIMO_DE_EPISTRONS  ;k++ )	  net_free (REDE_NEURONAL[k]);
}

/****************************************
 * Command line options
 ****************************************/

/*
le 1 rede (rede, arquivo)
***le todas as redes  (arquivobase)

salva 1 rede (rede, arquivo)
***salva todas redes (arquivobase)
    net_save (output_filename, net);

*/
