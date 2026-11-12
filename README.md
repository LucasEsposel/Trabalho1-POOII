Documentação Projeto Biblioteca.

	Aluno: Lucas Mantovani Esposel.


Identificação: No desenvolvimento do projeto foi implementado o Service Layer com o objetivo de descentralizar métodos do controller e evitar o acesso direto do controller pelo DAO. Os padrões de projeto utilizados foram o factory, que no contexto do projeto foi implementado para emitir mensagens de alerta, sucesso e erro. O segundo padrão de projeto é o observer, que identifica o valor do estoque em quantidade de livro e emite um aviso de quantos livros ainda existem para o usuário após uma modificação e o padrão de projeto strategy que foi implementado para estabelecer uma política de prazo quando um livro é emprestado, sendo exibido na tabela a data de devolução que o usuário deverá seguir.


Situação anterior: O principal problema que era possível observar no programa era a falta de uma estipulação de data limite para o empréstimo do livro. Com os novos padrões esse problema foi solucionado no package strategy.


Solução implementada: A solução implementada foi a refatoração da estrutura original do sistema, introduzindo a camada Service Layer e três padrões de projeto: Factor, Observer e Strategy.
1. Service Layer
   Foi criado o pacote service, contendo as classes UsuarioService, LivroService e EmprestimoService. Essas classes passaram a intermediar a comunicação entre os Controllers e os DAOs, concentrando as regras de negócio e as validações do programa. Com essa alteração, os Controllers deixaram de acessar diretamente os DAOs, passando a solicitar as operações necessárias ao Service.
   Trecho de código atualizado no Controller:


2. Padrão Factory Method
   Foi criado o pacote factory, contendo a classe abstrata AlertaFactory e suas implementações AlertaSucessoFactory, AlertaErroFactory e AlertaAvisoFactory. A classe abstrata define o método criarAlerta(), enquanto cada implementação determina qual tipo de alerta será criado. O método exibir() é responsável por configurar e apresentar a mensagem ao usuário.
   Trecho de código:

Assim, o Controller seleciona a factory de acordo com a situação, sem precisar criar e configurar diversos tipos de alertas.
3. Padrão Observer
   Foi criado o pacote observer, contendo a interface EstoqueObserver, a classe EstoqueNotificador e a implementação concreta AlertaEstoqueObserver. A interface define o método estoqueAlterado(), que é implementado pela classe concreta. Já a classe EstoqueNotificador mantém uma lista de observadores cadastrados e é responsável por enviar as notificações.
   Trecho de código:

No sistema, após um empréstimo ou uma devolução, o estoque é atualizado no banco de dados. Em seguida, o Controller recupera os dados atualizados do livro e chama o método notificar(). O observador recebe essa informação e apresenta um alerta com a quantidade disponível, destacando também quando o estoque chega a zero.
4. Padrão Strategy
   Foi criado o pacote strategy, contendo a interface PoliticaPrazo e duas implementações: PrazoPadrao e PrazoEstendido. A interface define o método calcularDataLimite(), enquanto cada implementação estabelece uma política diferente de prazo limite. A classe PrazoPadrao calcula sete dias a partir da data do empréstimo, enquanto PrazoEstendido calcula quinze dias.
   Trecho de código:

Trecho de código:

O EmprestimoService recebe a política selecionada e utiliza seu método para calcular a data limite de devolução. Foi adicionada uma ComboBox que permite ao usuário escolher entre as duas opções. A data calculada é armazenada no banco de dados e é exibida na tabela de empréstimos.

Justificativa e verificação: O código possui maior escalabilidade após as refatorações aplicadas, uma maior organização das classes e métodos presentes. O maior diferencial observável é a possibilidade de estipular uma data de empréstimo sendo possível definir o prazo padrão, de sete dias e o prazo estendido, de quinze dias. Outras alterações relevantes foram feitas, porém de caráter mais técnico, como a separação da exibição dos alertas realizados pelo factory ou até mesmo a implementação da camada de serviço para separar o DAO do controller, intermediando os pacotes.


Imagem abaixo registra a interface gráfica do gerenciamento de empréstimos

