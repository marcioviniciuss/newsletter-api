package com.marcio.newsletter_api.integrations.mail;

public final class EmailTemplateFactory {

    private EmailTemplateFactory() {
        // Construtor privado para impedir instanciação de classe utilitária
    }

    public static String buildConfirmationEmail(String subscriberName) {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Bem-vindo ao NewsNews</title>
            </head>
            <body style="margin: 0; padding: 0; background-color: #0b0f19; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;">
                <table align="center" border="0" cellpadding="0" cellspacing="0" width="100%%" style="max-width: 600px; margin: 40px auto; background-color: #111827; border: 1px solid #1f2937; border-radius: 16px; overflow: hidden;">
                    <!-- CABEÇALHO COM LOGO -->
                    <tr>
                        <td style="padding: 36px 32px 24px; text-align: center; border-bottom: 1px solid #1f2937;">
                            <div style="display: inline-block; background-color: #6366f1; color: #ffffff; width: 44px; height: 44px; line-height: 44px; border-radius: 12px; font-weight: 700; font-size: 20px;">
                                N
                            </div>
                            <h1 style="color: #ffffff; margin: 12px 0 4px; font-size: 22px; font-weight: 700; letter-spacing: -0.5px;">
                                News<span style="color: #818cf8;">News</span>
                            </h1>
                            <span style="display: inline-block; padding: 4px 12px; background-color: rgba(99, 102, 241, 0.1); border: 1px solid rgba(99, 102, 241, 0.2); border-radius: 20px; color: #a5b4fc; font-size: 11px; font-weight: 500;">
                                Feed Inteligente & Newsletter Diária
                            </span>
                        </td>
                    </tr>

                    <!-- CORPO PRINCIPAL -->
                    <tr>
                        <td style="padding: 32px 32px 24px;">
                            <h2 style="color: #f3f4f6; margin-top: 0; font-size: 18px; font-weight: 600;">
                                Olá, %s! 👋
                            </h2>
                            <p style="color: #9ca3af; font-size: 15px; line-height: 1.6; margin: 16px 0 24px;">
                                Sua inscrição no <strong>NewsNews</strong> foi confirmada. A partir de agora, o pulso do ecossistema tech — abrangendo arquitetura, código, cibersegurança e mercado — será entregue com curadoria no seu e-mail.
                            </p>

                            <!-- BOTÃO DE AÇÃO -->
                            <table border="0" cellpadding="0" cellspacing="0" width="100%%" style="margin: 28px 0;">
                                <tr>
                                    <td align="center">
                                        <a href="https://github.com/marcioviniciuss/newsletter-api" target="_blank" style="display: inline-block; padding: 14px 32px; background: linear-gradient(135deg, #6366f1 0%%, #7c3aed 100%%); color: #ffffff; text-decoration: none; font-size: 14px; font-weight: 600; border-radius: 8px;">
                                            Acessar Plataforma
                                        </a>
                                    </td>
                                </tr>
                            </table>

                            <p style="color: #6b7280; font-size: 13px; line-height: 1.5; margin: 0;">
                                Você está recebendo este e-mail porque submeteu seu endereço através da nossa página de inscrição.
                            </p>
                        </td>
                    </tr>

                    <!-- RODAPÉ -->
                    <tr>
                        <td style="padding: 20px 32px; background-color: #0b0f19; text-align: center; border-top: 1px solid #1f2937;">
                            <p style="color: #4b5563; font-size: 12px; margin: 0;">
                                © 2026 NewsNews API Engine • Desenvolvido por Márcio Vinícius
                            </p>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """.formatted(subscriberName);
    }
}
