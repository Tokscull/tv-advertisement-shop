import { useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';

import { userActions } from '_store';

export { Home };

function Home() {
    const dispatch = useDispatch();
    const { user: authUser } = useSelector(x => x.auth);
    const { users } = useSelector(x => x.users);

    useEffect(() => {
        dispatch(userActions.getAll());

    }, []);

    return (
        <div>
            <h1>Hi {authUser?.username}!</h1>
            <h3>List of all users:</h3>
            {users.length &&
                <ul>
                    {users.map(user =>
                        <li key={user.id}>
                            <b>username:</b> {user.username},
                            <b> email:</b> {user.email},
                            <b> roles:</b> {user.roles.map(role => <span>{role.name} </span>)}
                        </li>
                    )}
                </ul>
            }
            {users.loading && <div className="spinner-border spinner-border-sm"></div>}
            {users.error && <div className="text-danger">Error loading users: {users.error.message}</div>}
        </div>
    );
}
