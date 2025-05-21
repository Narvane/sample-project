import NextAuth, { NextAuthOptions } from 'next-auth';
import KeycloakProvider from 'next-auth/providers/keycloak';

export const authOptions: NextAuthOptions = {
    providers: [
        KeycloakProvider({
            clientId: `${process.env.KEYCLOAK_CLIENT_ID}`,
            clientSecret: '',
            issuer: `http://${process.env.KEYCLOAK_GENERAL_KEY_NAME}:${process.env.KEYCLOAK_PORT}/realms/sample-project-realm`,
        }),
    ],
    secret: process.env.NEXTAUTH_SECRET,
    callbacks: {
        async jwt({ token, account, user, profile }) {
            if (account?.access_token) {
                token.accessToken = account.access_token;
            }
            return token;
        },
        async session({ session, token }) {
            session.accessToken = token.accessToken as string;
            return session;
        },
    },
};

const handler = NextAuth(authOptions);

export { handler as GET, handler as POST };