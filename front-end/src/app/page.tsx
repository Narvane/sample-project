import {getServerSession} from "next-auth";
import { authOptions } from '@/app/api/auth/[...nextauth]/route';

export default async function Home() {
    const session = await getServerSession(authOptions);

    if (!session) {
        // Redirecionar para login se não estiver logado
        return (
            <a href="/api/auth/signin">
                Você precisa fazer login. Clique aqui.
            </a>
        );
    }

    return <div>Bem-vindo, {session.user?.name}</div>;
}
