# Projeto de Gerenciamento de Datashows

Este é um projeto simples em Clojure para gerenciar a alocação e desalocação de datashows. Ele permite consultar o status de um datashow, alocar ou desalocar um datashow com base no seu ID.

## Requisitos

Antes de rodar o código, você precisa ter o Clojure instalado em seu sistema. Para isso, siga os passos abaixo.

### Instalação do Clojure

1. **Instalar Clojure CLI** (recomendado):

   - Siga as instruções de instalação para o [Clojure CLI](https://clojure.org/guides/getting_started).
   - Para verificar se o Clojure está instalado corretamente, execute o comando:

     ```bash
     clojure -version
     ```

2. **Exemplo 1: Consultar o Status de um Datashow**:
    - Consultando status do datashow 1: :disponivel
    - Consultando status do datashow 3: :alocado

2. **Exemplo 2: Alocar um Datashow**:
    - Alocando datashow 1:
    - ID: 1 Status: :alocado
    - ID: 2 Status: :disponivel
    - ID: 3 Status: :alocado
    - ID: 4 Status: :disponivel

3. **Exemplo 3: Tentar Alocar um Datashow Já Alocado**:
    - Desalocando datashow 3:
    - ID: 1 Status: :alocado
    - ID: 2 Status: :disponivel
    - ID: 3 Status: :disponivel
    - ID: 4 Status: :disponivel

4. **Exemplo 4: Desalocar um Datashow**:
    - Desalocando datashow 3:
    - ID: 1 Status: :alocado
    - ID: 2 Status: :disponivel
    - ID: 3 Status: :disponivel
    - ID: 4 Status: :disponivel
