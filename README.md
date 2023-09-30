# Show do Milhão

## Identificação
**Componente Curricular:** Projeto Integrador <br/>
**Semestre Letivo:** 2023.1 <br/>
**Professor Responsável pelo PI:** Kamila Amélia Sousa Gomes

## Componentes Curriculares Integralizados
- CRT0026 - Estrutura de Dados Avançada
- CRT0029 - Probabilidade e Estatística
- CRT0427 - Fundamentos de Banco de Dados

## Professores das Disciplinas Integralizadas
- Lisieux Marie Marinho dos Santos Andrade
- Maria do Rosario Alves Patriota
- André Meireles de Andrade/Bruno de Castro Honorato Silva

## Linhas de Extensão
- Desenvolvimento tecnológico
- Metodologias e estratégias de ensino/aprendizagem
- Tecnologia da Informação

## Projeto 1
- Competências
- Comunicação
- Planejamento e organização
- Proatividade
- Integridade
- Trabalho em equipe

## Habilidades
- Prototipação
- Programação de computadores
- Estruturação de informações em Banco de Dados

## Descrição da Temática
Jogo de perguntas e respostas onde o conteúdo é gerenciado pela comunidade de jogadores. O jogo
permite aos usuários ampliação e teste dos seus conhecimentos, de forma simples e divertida. O
gerenciamento das perguntas pela comunidade manterá as questões atuais e corretas, como ocorre
em outros conteúdos geridos por comunidades com o Wikipédia. Jogos no estilo são famosos e fazem
sucesso em programas televisivos como o Show do Milhão e Who Wants to Be a Millionaire? <br/>
A equipe envolvida no projeto praticará o desenvolvimento de interface e interação para jogos, que
normalmente permitem elementos visuais mais coloridos e com aparência diferente dos demais programas de computador. As informações utilizadas no jogo deverão ser estruturadas, tanto nas classes
como no banco de dados corretamente, a fim de facilitar o desenvolvimento, manutenção, além de
possibilitar um bom desempenho do jogo. Assim, os conhecimento das estruturas de dados como,
filas e as formas normais do modelo relacional das tabelas serão aplicados e são de grande importância no projeto. As estatísticas coletadas do jogo ajudarão na busca por melhorias no jogo e maior
entendimento dos usuários.

## Produto
Jogo de perguntas, em que o objetivo é conseguir o prêmio de 1 milhão de reais, após responder
7 perguntas em uma partida. As perguntas são de múltipla escolha com quatro alternativas. Em
cada pergunta, o jogador poderá: responder a pergunta; parar de responder; errar a resposta; pedir a
eliminação de duas respostas erradas; ou denunciar a pergunta por enunciado, alternativas ou resposta
Projeto 1
incorreta. O jogador poderá pedir a eliminação de duas respostas erradas uma única vez por partida.
Se responder corretamente, o jogador passa para a próxima pergunta. De acordo com a pergunta
em que o jogador estiver, ele receberá um prêmio em dinheiro se o jogador responder corretamente,
parar ou errar a resposta de acordo com a tabela:

![Screen Shot 2023-04-04 at 23 56 22](https://user-images.githubusercontent.com/64222622/229969556-59711cd8-d038-474d-b0e8-ba6de3bdaae8.png)

O usuário precisa de uma conta para jogar. A conta do usuário possui as informações de:
• Nome;
• Nickname;
• Avatar (foto);
• Lista de perguntas adicionadas e aceitas;
• Lista de perguntas adicionadas e não aceitas;
• Número de partidas jogadas;
• Número total de perguntas respondidas;
• Premiação total de todas as partidas jogadas;
• Quantidade de utilizações da eliminação de duas alternativas;
O programa tem as seguintes funcionalidades:
1. Criação de conta;
2. Login;
3. Deslogar;
4. Edição de conta;
5. Remoção de conta;
6. Adição de pergunta. A pergunta é aceita quando cinco outros usuários escolhidos aleatoriamente aprovam-na;
7. Lista de perguntas adicionadas aceitas e não aceitas do usuário;
8. Edição de perguntas do usuário que foram adicionadas e não aceitas;
Projeto 1
9. Revisão de pergunta. Quando uma pergunta é denunciada por dois usuários, a pergunta é
enviada para cinco usuários verificarem o enunciado, alternativas e resposta correta;
10. Resposta das perguntas do jogo (necessárias telas de vitória e derrota do jogador);
11. Eliminação de duas respostas erradas;
12. Embaralhar as respostas em cada vez que pergunta é mostrada;
13. Hall da fama, com lista dos 10 jogadores com as maiores premiações totais do jogo;
14. Armazenamento das informações em banco de dados.

As sete perguntas que o jogador reponderá na partida serão buscadas no Banco de Dados (BD) e
armazenadas em uma lista simplesmente encadeada. É permitida a utilização de estruturas de listas
já existentes na linguagem ou framework empregado no desenvolvimento do sistema.
Todas as operações com o BD utilizarão apenas JDBC. MySQL deverá ser empregado no sistema.
A partir das informações dos jogadores, o programa obterá informações estatísticas para conhecer
melhor o perfil e o desempenho dos jogadores. Os seguintes dados serão avaliados estatisticamente:
• perguntas adicionadas por usuários;
• perguntas revisadas por usuários;
• partidas por usuário;
• quantia ganha por usuário;
• utilização de eliminação de duas respostas erradas.

As seguintes estatísticas serão obtidas:
• média amostral;
• mediana;
• variância;
• desvio padrão;
• intervalo de confiança para a média.

Objetivando identificar em qual pergunta o jogador encerrará a partida, e qual o motivo do encerramento, armazene a pergunta a qual o usuário encerrou a partida e motivo de encerramento da partida
pelo jogador (nas últimas 300 partidas), em seguida obtenha:
• proporção;
• distribuição binomial;
• distribuição normal padrão.
As informações estatísticas do programa serão acessadas apenas pela conta de administrador do
programa.

## Projeto 1
### Metodologia
• Definição dos requisitos do jogo;
• Definição das classes;
• Elaboração do modelo das tabelas do banco de dados;
• Testes com usuários;
• Correções de erros e problemas.
### Critérios do Produto
Ao final da disciplina a nota atribuída ao projeto será de acordo com as funcionalidades concluídas e
os seguintes itens:
1. Funcionalidades implementadas;
2. Interface e interação do programa;
3. Estrutura das tabelas no banco de dados;
4. Normalização das tabelas do banco de dados;
5. Consultas SQL;
6. Análises estatísticas
