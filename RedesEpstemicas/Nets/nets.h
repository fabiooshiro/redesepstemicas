#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <stdarg.h>
#include <string.h>
#include <time.h>
//#include <windows.h>

#include <GL/gl.h>   /* padrão do opengl */
#include <GL/glu.h>  /* padrão do opengl */
#include <GL/glut.h> /* sistema de janelas (não padrão) em cima do glu */
//#include <glui.h>  /* sistema de janelas (não padrão) em cima do glut */

#include "lwneuralnet.h" /* Redes Neuronais */
/*//windows adapter
#ifndef DWORD
#define WINAPI
typedef unsigned long DWORD;
typedef short WCHAR;
typedef void * HANDLE;
#define MAX_PATH PATH_MAX
typedef unsigned char BYTE;
typedef unsigned short WORD;
typedef unsigned int BOOL;
#endif
*/
/****************************************
 * Estado da aplicacao
 ****************************************/
int PAUSADO = 0; /*1 = pausado e 0 = despausado*/

/****************************************
 * Criação de tipos
 ****************************************/
typedef struct { float X; float Y; }	tipoCOORD;  


/****************************************
 * Epistrons
 ****************************************/
#define NUMERO_MAXIMO_DE_EPISTRONS 2000		/*Matriz de pesos entre os EPISTRONS*/


float		W									[NUMERO_MAXIMO_DE_EPISTRONS]		[NUMERO_MAXIMO_DE_EPISTRONS]; 
int			NUMERO_DE_WS_SAINDO_DO_EPISTRON		[NUMERO_MAXIMO_DE_EPISTRONS];
																					/*matriz de pesos da rede epistemica*/
tipoCOORD	POSICAO_DO_EPISTRON					[NUMERO_MAXIMO_DE_EPISTRONS];		/*	Coordenadas dos EPISTRONS	*/
tipoCOORD	POSICAO_DA_RESULTANTE_NO_GRAFICO	[NUMERO_MAXIMO_DE_EPISTRONS];		/*	Coordenadas da resultante do Epistron no gráfico*/
tipoCOORD	RESULTANTE_DO_EPISTRON				[NUMERO_MAXIMO_DE_EPISTRONS];
float		TAMANHO_DO_RAIO_NO_GRAFICO			[NUMERO_MAXIMO_DE_EPISTRONS];		/*	Raio no gráfico */



/****************************************
 *	Foco
 ****************************************/
#define NUMERO_DE_COORDENADAS_DO_FOCO  3	/*Dimensão do FOCO do Agente (Conhecimento da Rede Neuronal)*/

float	CENTROIDE    [NUMERO_MAXIMO_DE_EPISTRONS] [NUMERO_DE_COORDENADAS_DO_FOCO]; /*Coordenadas do centro do FOCO do agente*/
float	RAIO_DO_FOCO [NUMERO_MAXIMO_DE_EPISTRONS]; /*Raio do FOCO (que forma uma esfera/circunferencia) */




/****************************************
 * Redes Neuronais
 ****************************************/
typedef network_t*  TIPO_REDE_NEURONAL; /*tipo network_t, da biblioteca lwneuralnet.h*/
TIPO_REDE_NEURONAL		REDE_NEURONAL  [NUMERO_MAXIMO_DE_EPISTRONS];/*REDES NEURONAIS*/

#define	NUMERO_DE_CAMADAS_DA_REDE_NEURONAL 3	/*Número de Camadas da REDE -> MÍNIMO = 3, entrada, intermediária, saída*/
#define NUMERO_DE_NEURONIOS_NA_CAMADA_DE_ENTRADA	NUMERO_DE_COORDENADAS_DO_FOCO	/* Na camada de entrada o número de neuronios */
																					/*  é o numero de coordenadas do FOCO*/
#define NUMERO_DE_NEURONIOS_NA_CAMADA_DE_SAIDA 2

int		NUMERO_DE_NEURONIOS_NA_CAMADA[]		=	/*vetor necessário para lwneuralnet.h*/
											{	NUMERO_DE_NEURONIOS_NA_CAMADA_DE_ENTRADA, 
												1, /*numero de neuronios nas camadas intermediárias definido aqui*/
												NUMERO_DE_NEURONIOS_NA_CAMADA_DE_SAIDA			};

/****************************************
 * Formas e Cores
 ****************************************/
#define PRECISAO_DO_CIRCULO 100 /*pontos do polígono "circulo"*/
#define CIRCULO_CHEIO(x, y, r) CIRCULO(x, y, r, 1) 
#define CIRCULO_VAZIO(x, y, r) CIRCULO(x, y, r, 0) 

float COR_CINZA[3]			= {0.65,  0.65,  0.65 };/*cores, em vetores RGB*/
float COR_PRETO[3]			= {0.0,  0.0,  0.0 }; 
float COR_BRANCO[3]			= {1.0,  1.0,  1.0 };
float COR_VERMELHO[3]		= {0.7,  0.03,  0.03 };


/****************************************
 * Glut
 ****************************************/
#define NOME_DA_JANELA_PRINCIPAL "REDES EPISTÊMICAS AUTO-ORGANIZÁVEIS"
#define POSICAO_X_INICIAL_DA_JANELA 40 /*posicionamento inicial da janela*/
#define POSICAO_Y_INICIAL_DA_JANELA 40

int   LARGURA_DA_TELA = 600; /* DIMENSÃO INICIAL DA TELA*/
int   ALTURA_DA_TELA  = 600;



/****************************************
 * Contadores Globais
 ****************************************/
int contator_RODADAS_DO_ALGORITMO_DE_ATUALIZACAO_DA_REDE = 0;


/****************************************
 * Variáveis pós-definidas
 ****************************************/
int		ID_DA_JANELA_PRINCIPAL;
int		NUMERO_DE_EPISTRONS;
int		RODADAS_DO_ALGORITMO_DE_SIMULACA0_ANTES_DE_RENDERIZAR;
float	PONDERADOR_DA_QUANTIDADE_DE_TREINOS_NA_REDE_NEURONAL_EM_CIMA_DO_PESO_W;

int		ATUALIZA_A_TX_APRENDIZADO_A_CADA;
float	ATUALIZACAO_TX_APRENDIZADO;
float	TAXA_DE_APRENDIZADO_DA_REDE_EPISTEMICA;

int		GERA_WS_ALEATORIAMENTE;
int		ATUALIZAR_WS_EM_MODO_COMPETITIVO;

int		NORMALIZA_MATRIZ_W; /**/
int		NORMALIZA_MATRIZ_W_A_CADA; /**/

int		TEMPO_DE_REFRESH_DA_FUNCAO_TIMER_EM_MSECS;

int		EPISTRON_SORTEADO;

float	MAX_RAIO_FOCO;
float	LIMITE_SUPERIOR_COORDENADA_DO_FOCO;
int		GERA_FOCOS_ALEATORIAMENTE;

int		GERAR_LOG_A_CADA;
int		EXPORTAR_PARA_IMAGEM_A_CADA;

int		GERA_COORDENADAS_ALEATORIAMENTE;
float	CONSTANTE_QUE_PONDERA_A_DISTANCIA_QUE_O_EPISTRON_CAMINHA_EM_DIRECAO_A_RESULTANTE;
float	ATUALIZACAO_DA_CONSTANTE_QUE_PONDERA_A_DISTANCIA_QUE_O_EPISTRON_CAMINHA_EM_DIRECAO_A_RESULTANTE;
int		ATUALIZAR_A_CONSTANTE_QUE_PONDERA_A_DISTANCIA_QUE_O_EPISTRON_CAMINHA_EM_DIRECAO_A_RESULTANTE_A_CADA;


FILE *arq_log, *arq_foco, *arq_matriz_w, *arq_coordenadas;

/****************************************
 * Funções
 ****************************************/
void atualiza_rede_epistemica(int );
void cria_matriz_de_peso_w_aleatoriamente();
void cria_matriz_de_peso_w_aleatoriamente_de_forma_crescente();
void normaliza_matriz_de_pesos();

void gera_coordenadas_dos_epistrons_aleatoriamente();
void gera_coordenadas_dos_epistrons_em_grade();
tipoCOORD  CALCULA_RESULTANTE_DO_EPISTRON(int);
void CALCULA_POSICOES_DAS_RESULTANTES_NO_GRAFICO();
void CALCULA_NOVAS_POSICOES_DOS_EPISTRONS();
void CALCULA_RAIOS_DOS_EPISTRONS();
void display();

void inicia_redes_neuronais();
TIPO_REDE_NEURONAL cria_rede_neuronal_aleatoria();
float treina_rede_arg0_com_par_da_rede_arg1_arg2_vezes(int, int, double);
void libera_recursos_das_redes_neuronais();

void gera_focos_dos_agentes_aleatoriamente();
void gera_tupla_arg1_pertencente_ao_FOCO_da_rede_arg0(int, float *);
void inicia_glut_opengl(int *, char **);
void reshape (int, int);
void keyboard(unsigned char, int, int);
void mouse(int , int , int , int );

void CIRCULO(float , float , float , int );
void LINHA(float , float , float , float );
void TEXTO(float , float , char *, void *);

void inicia_semente_para_numeros_pseudo_aleatorios();
float numero_pseudo_aleatorio_positivo_menor_que(float );

int		dumpBMP(char *szName, int width, int height);
void	EXPORTA_TELA_ATUAL_PARA_IMAGEM();

/****************************************
 * Arquivos fonte
 ****************************************/

#include "foco.c"
#include "formas.c"
#include "modelo.c"
#include "redes_neuronais.c"
#include "renderizacao.c"
#include "tela.c"
