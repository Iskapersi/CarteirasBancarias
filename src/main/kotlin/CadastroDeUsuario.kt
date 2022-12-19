import Planos.*

fun Usuario.CadastroDeUsuario(): ContaCadastrada {
    fun geradorDeSenha(): String {
        val caracteres: String = ("0123456789")
        return (1..6).map { caracteres.random() }.joinToString("")
    }

    fun formatacpf(): String {

        val padraoEsperado: Regex = "([0-9]{3})([0-9]{3})([0-9]{3})([0-9]{2})".toRegex()
        val padraoFormatado = "$1.$2.$3-$4"

        return this.cpf.replace(padraoEsperado, padraoFormatado)
    }

    fun cadastrarNome() {
        println("Bem vindo ao Banco Genérico! Vamos configurar a sua conta.")
        println("Digite o seu primeiro nome:")
        usuario.nome = readln()
    }

    fun cadastrarSobrenome() {
        println("Digite o seu sobrenome:")
        usuario.sobrenome = readln()
    }

    fun cadastrarCpf() {

        var cpfValido: Boolean
        var cpfDigitado: Long?
        do {

            println("Digite o seu número do CPF (apenas números):")
            cpfDigitado = readln().toLongOrNull()
            usuario.cpf = cpfDigitado.toString()
            if (usuario.cpf.length != 11) {
                println("Erro, cpf inválido!")
                usuario.cpf = ""
                cpfValido = false
            } else {
                usuario.cpf = formatacpf()
                println(usuario.cpf)
                cpfValido = true
            }
        } while (!cpfValido)


    }

    fun gerarSenha() {
        println("Vamos gerar uma senha para sua conta. Anote e guarde em local seguro:")
        usuario.senha = geradorDeSenha()
        println(usuario.senha)
    }

    fun escolherPlanoInicial() {
        do {
            println(
                """Agora vamos escolher o plano adequado para você!
1 - Plano Normal (Faz saques, depósitos, paga boletos)
2 - Plano Digital (Faz investimentos, transfere Pix e paga boletos)
3 - Plano Premium (Faz tudo que os Planos Normal e Plano Digital oferecem)
""".trimMargin()
            )
            val planoescolhido = readln().toIntOrNull()
            when (planoescolhido) {
                1 -> plano = NORMAL
                2 -> plano = DIGITAL
                3 -> plano = PREMIUM
                else -> ERRO
            }
        } while (plano == ERRO)
    }

    cadastrarNome()
    cadastrarSobrenome()
    cadastrarCpf()
    gerarSenha()
    escolherPlanoInicial()

    return ContaCadastrada(usuario.nome, usuario.sobrenome, usuario.senha, usuario.cpf, usuario.plano, usuario.saldo)
}