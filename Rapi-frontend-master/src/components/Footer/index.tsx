import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import '@umijs/max';
const Footer: React.FC = () => {
  const defaultMessage = '锐 2024-07-02';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'ant-desgin',
          title: 'ant-desgin',
          href: 'https://pro.ant.design',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: 'https://github.com/Rui030517/Rapi',
          blankTarget: true,
        },
        {
          key: 'Rapi接口',
          title: 'Rapi接口',
          href: 'https://github.com/Rui030517/Rapi',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
