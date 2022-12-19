import Planos.*
import java.text.NumberFormat
import java.util.*
import kotlin.random.Random

class Conta(private val usuarioCadastrado: MutableList<ContaCadastrada> = mutableListOf()) {

    val nomeDoBanco = "Banco Genérico"
    private var dinheiroGuardado: Double = 0.00
    private var extrato = "===========EXTRATO===========\n" +
            "TIPO===========VALOR==========SALDO\n"

    fun adicionarContaNova(novaConta: ContaCadastrada) {
        usuarioCadastrado.add(novaConta)
    }

    fun menuInicial() {
        val item = usuarioCadastrado[0]
        item.let {
            when (it.plano) {
                NORMAL -> menuNormal()
                DIGITAL -> menuDigital()
                PREMIUM -> menuPremium()
                ERRO -> println("Erro desconhecido, entre em contato com o suporte")
            }
        }
    }

    private fun menuPremium() {
        println(
            """===============MENU PREMIUM===============
Bem vindo ao $nomeDoBanco! Escolha a opção desejada para navegar o menu:
1 - Acessar carteira física
2 - Acessar carteira digital
3 - Configurações da conta
0 - Sair
""".trimMargin()
        )
        val opcaoEscolhida = readln().toIntOrNull()
        when (opcaoEscolhida) {
            1 -> menuNormal()
            2 -> menuDigital()
            3 -> {
                configConta()
                menuPremium()
            }

            0 -> {
                println("Tem certeza que deseja sair? S/N")
                val sair = readln().uppercase()
                when (sair) {
                    "S" -> {
                        println("Muito obrigado por usar o $nomeDoBanco. Até a próxima!")
                        return
                    }

                    "N" -> {
                        menuPremium()
                    }

                    else -> {
                        println("Opção Inválida, retornando ao menu.")
                        menuPremium()
                    }
                }
            }

            else -> {
                println("Opção inválida, tente novamente.")
                menuPremium()
            }
        }
    }

    private fun menuDigital() {
        println(
            """===============CARTEIRA DIGITAL===============
Bem vindo ao $nomeDoBanco! Escolha a opção desejada para navegar o menu:
1 - Extrato
2 - Receber
3 - Transferência Pix
4 - Investir
5 - Guardar/Retirar
6 - Pagar Boleto
7 - Configurações da Conta
8 - Sair
""".trimMargin()
        )

        val opcaoEscolhida = readln().toIntOrNull()
        when (opcaoEscolhida) {
            1 -> {
                imprimirExtrato()
                menuDigital()
            }

            2 -> {
                receber()
                menuDigital()
            }

            3 -> {
                transferenciaPix()
                menuDigital()
            }

            4 -> {
                investir()
                menuDigital()
            }

            5 -> {
                guardarRetirar()
                menuDigital()
            }

            6 -> {
                pagarBoleto()
                menuDigital()
            }

            7 -> {
                configConta()
                menuDigital()
            }

            8 -> {
                val item = usuarioCadastrado[0]
                item.let {
                    if (it.plano == PREMIUM) {
                        menuPremium()
                    } else {
                        println("Tem certeza que deseja sair? S/N")
                        val sair = readln().uppercase()
                        when (sair) {
                            "S" -> {
                                println("Muito obrigado por usar o $nomeDoBanco. Até a próxima!")
                                return
                            }

                            "N" -> {
                                menuDigital()
                            }

                            else -> {
                                println("Opção Inválida, retornando ao menu.")
                                menuDigital()
                            }
                        }
                    }
                }
            }

            else -> {
                println("Opção inválida, tente novamente.")
                menuDigital()
            }
        }
    }

    private fun menuNormal() {
        println(
            """===============CARTEIRA FÍSICA===============
Bem vindo ao $nomeDoBanco! Escolha a opção desejada para navegar o menu:
1 - Extrato
2 - Depósito
3 - Saque
4 - Pagar Boleto
5 - Configurações da Conta
6 - Sair
""".trimMargin()
        )

        val opcaoEscolhida = readln().toIntOrNull()
        when (opcaoEscolhida) {
            1 -> {
                imprimirExtrato()
                menuNormal()
            }

            2 -> {
                deposito()
                menuNormal()
            }

            3 -> {
                saque()
                menuNormal()
            }

            4 -> {
                pagarBoleto()
                menuNormal()
            }

            5 -> {
                configConta()
                menuNormal()
            }

            6 -> {
                val item = usuarioCadastrado[0]
                item.let {
                    if (it.plano == PREMIUM) {
                        menuPremium()
                    } else {
                        println("Tem certeza que deseja sair? S/N")
                        val sair = readln().uppercase()
                        when (sair) {
                            "S" -> {
                                println("Muito obrigado por usar o $nomeDoBanco. Até a próxima!")
                                return
                            }

                            "N" -> {
                                menuNormal()
                            }

                            else -> {
                                println("Opção Inválida, retornando ao menu.")
                                menuNormal()
                            }
                        }
                    }
                }
            }

            else -> {
                println("Opção inválida, tente novamente.")
                menuNormal()
            }
        }
    }

    private fun receber() {
        val item = usuarioCadastrado[0]
        item.let {
            println("Quanto você receberá?")
            val receber = readln().toDoubleOrNull()
            if (receber == null) {
                println("Digite um valor válido.")
                receber()
            } else {
                println("Compatilhe o código para receber: ${it.nome.lowercase() + receber.toInt()}")
                println("Digite sua senha para confirmar:")
                val senhaDigitada = readln()
                if (senhaDigitada == it.senha) {
                    it.saldo += receber
                    extrato += "*Recebeu transferência cod: ${it.nome.lowercase() + receber.toInt()}\n"
                    extratoAutomatico("TRANSFERÊNCIA", receber, true)
                    println("Recebeu ${receber.formataMonetario()} com sucesso.")
                } else {
                    println("Senha errada, retornando.")
                }
            }
        }
    }

    private fun guardarRetirar() {
        println(
            """Seu valor guardado é de ${dinheiroGuardado.formataMonetario()}.
O que deseja fazer?
1 - Guardar Dinheiro
2 - Resgatar Dinheiro
3 - Sair
""".trimMargin()
        )
        val opcaoEscolhida = readln().toIntOrNull()
        when (opcaoEscolhida) {
            1 -> guardar()
            2 -> retirar()
            3 -> {}
            else -> {
                println("Opção inválida, tente novamente.")
                guardarRetirar()
            }
        }
    }

    private fun configConta() {
        println(
            """
1 - Mudar tipo de conta
2 - Mudar senha
3 - Voltar
""".trimIndent()
        )
        val opcaoEscolhida = readln().toIntOrNull()
        when (opcaoEscolhida) {
            1 -> mudarConta()
            2 -> mudarSenha()
            3 -> {}
            else -> {
                println("Opção inválida, tente novamente.")
                configConta()
            }
        }
    }

    private fun mudarSenha() {
        val item = usuarioCadastrado[0]
        item.let {
            println("Digite sua senha atual:")
            val senhaAtual = readln()
            if (senhaAtual != it.senha) {
                println("Senha errada, retornando.")
            } else {
                println("Digite a nova senha:")
                val novaSenha = readln()
                println("Confirme a nova senha:")
                val confirmaSenha = readln()
                if (novaSenha == confirmaSenha) {
                    it.senha = novaSenha
                    println("Senha alterada com sucesso!")
                } else {
                    println("Senha errada, retornando.")
                }
            }
        }
    }

    private fun mudarConta() {
        val item = usuarioCadastrado[0]
        item.let {
            println("Sua conta é do tipo ${it.plano}")
            println(
                """
Para qual plano deseja mudar?
1 - Plano Premium
2 - Plano Digital
3 - Plano Normal
0 - Cancelar
""".trimIndent()
            )
            val opcaoEscolhida = readln().toIntOrNull()
            when (opcaoEscolhida) {
                1 -> {
                    if (it.plano != PREMIUM) {
                        it.plano = PREMIUM
                        println("Agora você é um Cliente Premium, ao sair do menu da sua conta, você terá acesso ao Menu Premium.")
                    } else {
                        println("Você já é um Cliente Premium.")
                    }
                }

                2 -> {
                    if (it.plano != DIGITAL) {
                        it.plano = DIGITAL
                        println("Agora você é um Cliente Digital, aproveite as funcionalidades!")
                    } else {
                        println("Você já é um Cliente Digital.")
                    }
                    menuDigital()
                }

                3 -> {
                    if (it.plano != NORMAL) {
                        it.plano = NORMAL
                        println("Agora você é um Cliente Normal, aproveite as funcionalidades!")
                    } else {
                        println("Você já é um Cliente Normal.")
                    }
                    menuNormal()
                }

                0 -> {}
                else -> {
                    println("Opção inválida, tente novamente.")
                    mudarConta()
                }
            }
        }
    }

    private fun saque() {
        val item = usuarioCadastrado[0]
        item.let {
            println("Digite o valor do saque:")
            val saque = readln().toDoubleOrNull()
            if (saque == null) {
                println("Digite um valor válido.")
                saque()
            } else if (saque > it.saldo) {
                println("Impossível sacar ${saque.formataMonetario()}, saldo de ${it.saldo.formataMonetario()}.")
            } else {
                println("Digite sua senha para confirmar:")
                val senhaDigitada = readln()
                if (senhaDigitada == it.senha) {
                    it.saldo -= saque
                    extratoAutomatico("SAQUE", saque, false)
                    println("Saque de ${saque.formataMonetario()} feito com sucesso.")
                } else {
                    println("Senha errada, retornando.")
                }
            }
        }
    }

    //funções plano normal
    private fun imprimirExtrato() {
        val item = usuarioCadastrado[0]
        item.let {
            println("===============INÍCIO=================")
            print(extrato)
            println("                    TOTAL ${it.saldo.formataMonetario()}")
            println("=================FIM==================")
        }
    }

    private fun Double.formataMonetario(): String {
        return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(this)
    }

    private fun extratoAutomatico(tipoMovimento: String, valorMovido: Double, entraSai: Boolean) {
        val item = usuarioCadastrado[0]
        item.let {
            if (entraSai) {
                extrato += "$tipoMovimento      +${valorMovido.formataMonetario()}     ${it.saldo.formataMonetario()}\n"
            } else {
                extrato += "$tipoMovimento      -${valorMovido.formataMonetario()}     ${it.saldo.formataMonetario()}\n"
            }
        }
    }

    private fun deposito() {
        val item = usuarioCadastrado[0]
        item.let {
            println("Digite o valor do depósito:")
            val deposito = readln().toDoubleOrNull()
            if (deposito == null) {
                println("Digite um valor válido.")
                deposito()
            } else {
                println("Digite sua senha para confirmar:")
                val senhaDigitada = readln()
                if (senhaDigitada == it.senha) {
                    it.saldo += deposito
                    extratoAutomatico("DEPÓSITO", deposito, true)
                    println("Depositados ${deposito.formataMonetario()} com sucesso.")
                } else {
                    println("Senha errada, retornando.")
                }
            }
        }
    }

    private fun pagarBoleto() {
        println("Digite o código do boleto a pagar:")
        val codBoleto = readln()
        println("Confirme o valor do boleto a pagar:")
        val valBoleto = readln().toDoubleOrNull()
        val item = usuarioCadastrado[0]
        item.let {
            if (valBoleto == null) {
                println("Valor inválido, retornando.")
            } else if (valBoleto > it.saldo) {
                println("Impossível pagar o boleto de ${valBoleto.formataMonetario()}, saldo de ${it.saldo.formataMonetario()}")
            } else {
                println("Digite a sua senha para confirmar o pagamento:")
                val senhaDigitada = readln()
                if (senhaDigitada == it.senha) {
                    it.saldo -= valBoleto
                    extrato += "*Pagamento de boleto $codBoleto \n"
                    extratoAutomatico("BOLETO", valBoleto, false)
                    println("Boleto de ${valBoleto.formataMonetario()} pago com sucesso!")
                } else {
                    println("Senha errada, retornando.")
                }
            }
        }
    }

    //funções plano digital
    private fun transferenciaPix() {
        println("Digite o código pix a transferir:")
        val codPix = readln()
        println("Confirme o valor da transferência:")
        val valPix = readln().toDoubleOrNull()
        val item = usuarioCadastrado[0]
        item.let {
            if (valPix == null) {
                println("Valor inválido, retornando.")
            } else if (valPix > it.saldo) {
                println("Impossível realizar a transferência de ${valPix.formataMonetario()}, saldo de ${it.saldo.formataMonetario()}.")
            } else {
                println("Digite a sua senha para confirmar a transferência:")
                val senhaDigitada = readln()
                if (senhaDigitada == it.senha) {
                    it.saldo -= valPix
                    extrato += "*Transferência pix $codPix \n"
                    extratoAutomatico("PIX", valPix, false)
                    println("Transferência de ${valPix.formataMonetario()} realizada com sucesso!")
                } else {
                    println("Senha errada, retornando.")
                }
            }
        }
    }

    private fun investir() {
        println("Digite o nome da Ação a investir:")
        val nomeAcao = readln()
        println("Quanto deseja investir?")
        val valAcao = readln().toDoubleOrNull()
        val item = usuarioCadastrado[0]
        item.let {
            if (valAcao == null) {
                println("Valor inválido, retornando.")
            } else if (valAcao > it.saldo) {
                println("Impossível pagar o boleto de ${valAcao.formataMonetario()}, saldo de ${it.saldo.formataMonetario()}")
            } else {
                println("Digite a sua senha para confirmar o pagamento:")
                val senhaDigitada = readln()
                if (senhaDigitada == it.senha) {

                    val aleatorio = Random.nextDouble(0.01, 0.11)
                    val ganhouPerdeu = Random.nextBoolean()
                    val investLiquido: Double = aleatorio * valAcao

                    if (ganhouPerdeu) {
                        it.saldo += investLiquido
                        extrato += "*Investimento em ação $nomeAcao \n"
                        extratoAutomatico("INVESTIMENTO", investLiquido, true)
                        println("Parabéns! Ganhou ${investLiquido.formataMonetario()} em seu investimento!")
                    } else {
                        it.saldo -= valAcao
                        extrato += "*Investimento em ação $nomeAcao \n"
                        extratoAutomatico("INVESTIMENTO", investLiquido, false)
                        println("Que pena! Perdeu ${investLiquido.formataMonetario()} em seu investimento!")
                    }
                } else {
                    println("Senha errada, retornando.")
                }
            }
        }
    }

    private fun guardar() {
        val item = usuarioCadastrado[0]
        item.let {
            println("Digite o valor para guardar, ele será removido do saldo e poderá ser resgatado a qualquer momento:")
            val valorGuardar = readln().toDoubleOrNull()
            if (valorGuardar == null) {
                println("Digite um valor válido.")
                guardar()
            } else if (valorGuardar > it.saldo) {
                println("Impossível guardar ${valorGuardar.formataMonetario()}, saldo de ${it.saldo.formataMonetario()}")
                guardar()
            } else {
                println("Digite a sua senha para confirmar:")
                val senhaDigitada = readln()
                if (senhaDigitada == it.senha) {
                    it.saldo -= valorGuardar
                    dinheiroGuardado += valorGuardar
                    extrato += "*Guardar dinheiro\n"
                    extratoAutomatico("GUARDAR", valorGuardar, false)
                    println("o valor de ${valorGuardar.formataMonetario()} foi guardado com sucesso.")
                } else {
                    println("Senha errada, retornando.")
                }
            }
        }
    }

    private fun retirar() {
        val item = usuarioCadastrado[0]
        item.let {
            println("Digite o valor para resgatar:")
            val valorRetirar = readln().toDoubleOrNull()
            if (valorRetirar == null) {
                println("Digite um valor válido.")
                retirar()
            } else if (valorRetirar > dinheiroGuardado) {
                println("Impossível resgatar ${valorRetirar.formataMonetario()}, valor guardado de ${dinheiroGuardado.formataMonetario()}")
                retirar()
            } else {
                println("Digite a sua senha para confirmar:")
                val senhaDigitada = readln()
                if (senhaDigitada == it.senha) {
                    it.saldo += valorRetirar
                    dinheiroGuardado -= valorRetirar
                    extrato += "*Resgatar dinheiro\n"
                    extratoAutomatico("RESGATE", valorRetirar, true)
                    println("o valor de ${valorRetirar.formataMonetario()} foi resgatado com sucesso.")
                } else {
                    println("Senha errada, retornando.")
                }
            }
        }
    }
}