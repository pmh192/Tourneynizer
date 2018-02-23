import AccountCreationForm from './AccountCreationForm'
import React from 'react';
import { renderer } from 'react-test-renderer';

const accountCreationForm = {
	email: 'test@test.com',
	password: 'abc123',
	confirmPassword: 'abc321',
	firstName: 'test',
	lastName: 'test',
}

describe('accountCreationForm component renders correctly', () => {
  it('renders correctly', () => {
  	const accountCreationForm = {
		email: 'test@test.com',
		password: 'abc123',
		confirmPassword: 'abc321',
		firstName: 'test',
		lastName: 'test',
	}
    const rendered = renderer.create(
      <AccountCreationForm accountCreationForm={accountCreationForm} />
    );
    expect(rendered.toJSON()).toMatchSnapshot();
  });
});